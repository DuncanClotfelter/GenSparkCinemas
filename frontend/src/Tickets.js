import myFilms from "./myfilms.png";
import React, { useEffect, useState } from 'react';
import axios from 'axios';

const Tickets = () => {
    const [result, setResults] = useState([]);

    useEffect(() => {
        axios.get(`http://localhost:8080/myfilms`)
        .then(res => {
            alert(res.data);
            setResults(res.data);
        })
    });

    return (
        <>
            <img src={myFilms} class="headerImg"></img>
            {
                
            }
        </>
    );
};

export default Tickets;