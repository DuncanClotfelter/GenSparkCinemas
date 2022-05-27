import NavBar from "./Components/NavBar";
import SideBar from "./Components/SideBar";
import ProductCard from "./Components/ProductCard";
import nowShowing from "./Components/now-showing.gif";

import Home from "./Home";
import Tickets from "./Tickets";
import ContactUs from "./ContactUs";

import './App.css';

function App() {
  return (
    <>
    <div><NavBar /></div>
    <SideBar side="left"></SideBar>
    {getPageContents(window.location.pathname)}
    <SideBar side="right"></SideBar>
    </>
  );
}

function getPageContents(page) {
  switch(page) {
    case "/":
      return <Home />;
    case "/tickets":
      return <Tickets />;
    case "/contactus":
      return <ContactUs />;
  }
}

export default App;
