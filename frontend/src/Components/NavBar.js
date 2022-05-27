import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './NavBar.css';

const NavBar = (props) =>{
  const [result, setResults] = useState([]);

  useEffect(() => {
    axios.get(`http://localhost:8080/`)
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
        <a href="http://localhost:8080/oauth2/authorization/github">
          <img class={result != -1 ? 'hidden' : ''} src="https://cloud.githubusercontent.com/assets/194400/11214293/4e309bf2-8d38-11e5-8d46-b347b2bd242e.png"></img>
        </a>
        <span class={result == -1 ? 'hidden' : ''}>Welcome!</span>
      </li>
		</ul>
  )
}

function isActive(page) {
  return window.location.pathname == page ? 'active' : '';
}

export default NavBar;