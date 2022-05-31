import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './ProductCard.css';
import PaypalButton from "./PaypalButton";


const ProductCard = (props) => {
    return (
        <div class="card">
        <img src={props['image']}></img>
        <div class="hideOnLoggedOut hidden"><PaypalButton id={props['id']} /></div>
        <div class="hideOnLoggedIn"><button>Buy Now</button></div>
        </div>
    )
  }
  
  export default ProductCard;