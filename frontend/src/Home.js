import ProductCard from "./Components/ProductCard";
import nowShowing from "./Components/now-showing.gif";

const Home = () => {
    return (
        <><img src={nowShowing} className="headerImg"></img><table><tbody>
            <tr>
                <td><ProductCard
                    image="https://upload.wikimedia.org/wikipedia/en/0/00/Spider-Man_No_Way_Home_poster.jpg"
                    price="4.99"
                    name="Spodermin"
                    id="0"
                ></ProductCard></td>
                <td><ProductCard
                    image="https://upload.wikimedia.org/wikipedia/en/5/5f/Bee_Movie_%282007_animated_feature_film%29.jpg"
                    price="4.99"
                    name="Scary Movie"
                    id="1"
                ></ProductCard></td>
                <td><ProductCard
                    image="https://upload.wikimedia.org/wikipedia/en/f/ff/The_Batman_%28film%29_poster.jpg?20220411014317"
                    price="4.99"
                    name="COVID Documentary"
                    id="2"
                ></ProductCard></td>
            </tr>
            </tbody></table></>
    );
};

export default Home;