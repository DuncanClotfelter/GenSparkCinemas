import React from 'react';
import left from "./left.png";
import right from "./right.png";
import './SideBar.css';

const SideBar = (props) =>{
    var imgSrc = props['side'] == "left" ? left : right;
    return (
        <img src={imgSrc} className={`${props['side']} sidebar`} ></img>
    )
}

export default SideBar;