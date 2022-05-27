import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './ProductCard.css';
import PaypalButton from "./PaypalButton";


const ProductCard = (props) => {
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
        <div class="card">
        <img src={props['image']}></img>
        <p class={result == -1 ? 'hiddenx' : ''}><PaypalButton id={props['id']} /></p>
        <p class={result != -1 ? 'hidden' : ''}><button>Buy Now</button></p>
        </div>
    )
  }
  
  export default ProductCard;