import React, { useEffect, useState } from 'react';
import { Navbar, Container, Nav, Form, FormControl, Button } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
import './NavBar.css';

const NavBar = (props) => {
  const [result, setResults] = useState([]);

  useEffect(() => {
    axios.get(`http://localhost:8080/api`)
    .then(res => {
        var id = res.data;
        if(res.status < 200 || res.status >= 300) {id = -1;}
        setResults(id);
        var loggedIn = id != -1;
        alert(`Result: ${res.data}, Logged In: ${loggedIn}`);
        var hiddenOnLoggedInEles = document.getElementsByClassName("hideOnLoggedIn");
        var hiddenOnLoggedOutEles = document.getElementsByClassName("hideOnLoggedOut");
        for (const ele of hiddenOnLoggedInEles.children) {
          if(loggedIn) {ele.classList.add("hidden");}
          else {ele.classList.remove("hidden");}
        }
        for (const ele of hiddenOnLoggedOutEles.children) {
          if(loggedIn) {ele.classList.remove("hidden");}
          else {ele.classList.add("hidden");}
        }
    });
  });

  return (
    <Navbar bg="light" expand="lg">
      <Container>
        <Navbar.Brand href="#home">GenSpark Cinemas</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/">Home</Nav.Link>
            <Nav.Link href="/tickets">Tickets</Nav.Link>
            <Nav.Link href="/contact">Contact Us</Nav.Link>
          </Nav>
          <div className="d-flex">
            <a href="/oauth2/authorization/github">
              <img className="hideOnLoggedIn" src="https://cloud.githubusercontent.com/assets/194400/11214293/4e309bf2-8d38-11e5-8d46-b347b2bd242e.png"></img>
            </a>
            <span className="hideOnLoggedOut hidden">Welcome!</span>
          </div>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

/*const NavBar = (props) =>{
  const [result, setResults] = useState([]);

  useEffect(() => {
    axios.get(`http://localhost:8080/api`)
    .then(res => {
        var id = res.data;
        if(res.status < 200 || res.status >= 300) {id = -1;}
        setResults(id);
    })
  });

  return (
    <ul id="navBar">
			<li><a class={isActive('/')} href="/">Movies</a></li>
			<li><a class={isActive('/tickets')} href="tickets">My Tickets</a></li>
			<li><a class={isActive('/contactus')} href="contactus">Contact Us</a></li>
      <li class="login">
        <a href="/oauth2/authorization/github">
          <img class={result != -1 ? 'hidden' : ''} src="https://cloud.githubusercontent.com/assets/194400/11214293/4e309bf2-8d38-11e5-8d46-b347b2bd242e.png"></img>
        </a>
        <span class={result == -1 ? 'hidden' : ''}>Welcome!</span>
      </li>
		</ul>
  )
}

function isActive(page) {
  return window.location.pathname == page ? 'active' : '';
}*/

export default NavBar;