package net.clotfelter.duncan.ShoppingCartDemo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.clotfelter.duncan.ShoppingCartDemo.entities.Cart;
import net.clotfelter.duncan.ShoppingCartDemo.entities.products.Film;
import net.clotfelter.duncan.ShoppingCartDemo.entities.products.Product;
import net.clotfelter.duncan.ShoppingCartDemo.entities.User;
import net.clotfelter.duncan.ShoppingCartDemo.entities.products.Ticket;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@CrossOrigin
@RestController
public class ShoppingController extends WebSecurityConfigurerAdapter {
    @Value("${paypal.client.id}")
    private String clientId;
    @Value("${paypal.client.secret}")
    private String clientSecret;
    @Value("${paypal.mode}")
    private String mode;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests(a -> a
                        .antMatchers("/", "/error", "/webjars/**", "/index.html").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login();
        // @formatter:on
    }

//    @RequestMapping("/")
//    public String test(HttpServletResponse httpResponse) throws Exception {
//        httpResponse.sendRedirect("/");
//        return "hey";
//    }

    @CrossOrigin
    @GetMapping("/api")
    public String mainPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return "-1";
    }

    @CrossOrigin
    @GetMapping("/api/myfilms")
    public List getUserTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        List<String[]> toReturn = new ArrayList<>();
        try (var session = HibernateAnnotationUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Ticket.class);
            criteria.add(Restrictions.like("user", authentication.getName()));
            return criteria.list().stream().map(t -> getTicketData((Ticket)t)).toList();
        }
    }

    private String[] getTicketData(Ticket t) {
        return new String[] {
                t.getFilm().getName(),
                t.getFilm().getShowTime(),
                t.getFilm().getDescription(),
                String.valueOf(t.getUnits())
        };
    }

    @GetMapping("/api/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    //TODO send current user ID in purchase data
    @CrossOrigin
    @GetMapping("/api/confirmpurchase")
    public String confirmPurchase(@RequestParam String payment, @RequestParam int show) {
        try {
            String encoding = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
            var client = HttpClient.newHttpClient();

            var request = HttpRequest.newBuilder(
                            URI.create("https://api-m.sandbox.paypal.com/v1/payments/payment/" + payment))
                    .header("accept", "application/json")
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + encoding)
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() < 200 || response.statusCode() >= 300) {
                return purchaseError();
            }

            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

            if(!validPurchase(jsonObject)) {
                return purchaseError();
            } else {
                return completePurchase(response.body(), payment, show, "duncan@clotfelter.net");
            }
        } catch(Exception e) {
            e.printStackTrace();
            return purchaseError();
        }
    }

    private String completePurchase(String json, String paymentId, int filmId, String email) {
        var session = HibernateAnnotationUtil.getSessionFactory().getCurrentSession();
        var tx = session.beginTransaction();
        Film toWatch = session.get(Film.class, filmId);

        Ticket soldTicket = new Ticket();
        soldTicket.setId(paymentId);
        soldTicket.setFilm(toWatch);
        soldTicket.setUnits(1);
        soldTicket.setUser(SecurityContextHolder.getContext().getAuthentication().getName());
        soldTicket.setPurchase(json);

        session.saveOrUpdate(soldTicket);
        tx.commit();

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("gensparkmovies@gmail.com");
            message.setTo(email);
            message.setSubject("Thank you for your purchase!");
            message.setText("Don't forget to collect your free popcorn! It's on the house!");
            ShoppingCartDemoApplication.getJavaMailSender().send(message);
        } catch(Exception e) {e.printStackTrace();}

        return "Thanks for your purchase!";//TODO template
    }

    //TODO more validation
    private boolean validPurchase(JsonObject purchase) {
        return (
                purchase.get("state").getAsString().equals("approved")
        );
    }

    //TODO template
    @GetMapping("/error")
    private String purchaseError() {
        return "We are sorry, but there was an issue validating your purchase. Please try again, or contact us <a href='http://localhost:8080/contactus'>here</a>";
    }

    @GetMapping("/productsbyid")
    public List<Product> getProductsById(@RequestParam int id) {
        try (var session = HibernateAnnotationUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            var criteria = builder.createQuery(Product.class);
            Root<Product> productRoot = criteria.from(Product.class);
            criteria.where(builder.equal(productRoot.get("id"), id));
            return session.createQuery(criteria).getResultList();
        }
    }

    @GetMapping("/productsbyname")
    public List getProductsByName(@RequestParam String name) {
        try (var session = HibernateAnnotationUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Product.class);
            criteria.add(Restrictions.ilike("name", "%"+name+"%"));
            return criteria.list();
        }
    }

    @GetMapping("/productsbytype")
    public List<Object> getProductsByType(@RequestParam String type) {
        try (var session = HibernateAnnotationUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            var criteria = builder.createQuery();
            criteria.select(criteria.from(Class.forName("net.clotfelter.duncan.ShoppingCartDemo.entities.products."+type)));
            return session.createQuery(criteria).getResultList();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @PostMapping("/cart")
    public void createCart() {
        var session = HibernateAnnotationUtil.getSessionFactory().getCurrentSession();
        var tx = session.beginTransaction();
        User u = (User) ShoppingCartDemoApplication.context.getBean("OnlyUser");
        u = session.load(User.class, u.getId());
        if(u.getCart() == null) {
            Cart c = new Cart();
            c.getProducts().add((Product) ShoppingCartDemoApplication.context.getBean("book"));
            c.getProducts().add((Product) ShoppingCartDemoApplication.context.getBean("apparel"));
            u.setCart(c);
            session.saveOrUpdate(c);
            session.saveOrUpdate(u);
            System.out.println("Successfully created new cart: "+c);
        } else {
            System.out.println("Failed to create new cart: already exists!");
        }
        tx.commit();
    }

    @GetMapping("/cart")
    public Cart getCart() {
        var session = HibernateAnnotationUtil.getSessionFactory().getCurrentSession();
        var tx = session.beginTransaction();
        User u = (User) ShoppingCartDemoApplication.context.getBean("OnlyUser");
        u = session.load(User.class, u.getId());
        System.out.println("Successfully read cart: "+u.getCart());
        return u.getCart();
    }

    @PutMapping("/cart")
    public void updateCart(@RequestBody Cart updatedCart) {
        var session = HibernateAnnotationUtil.getSessionFactory().getCurrentSession();
        var tx = session.beginTransaction();
        session.saveOrUpdate(updatedCart);
        tx.commit();
        System.out.println("Successfully updated cart: "+updatedCart);
    }

    @DeleteMapping("/cart")
    public void deleteCart() {
        var session = HibernateAnnotationUtil.getSessionFactory().getCurrentSession();
        var tx = session.beginTransaction();
        User u = (User) ShoppingCartDemoApplication.context.getBean("OnlyUser");
        u = session.load(User.class, u.getId());
        Cart toDelete = u.getCart();
        u.setCart(null);
        session.saveOrUpdate(u);
        session.delete(toDelete);
        tx.commit();
        System.out.println("Successfully deleted cart: "+u.getCart());
    }
}
