import NavBar from "./Components/NavBar";
import SideBar from "./Components/SideBar";
import { BrowserRouter as Router, Routes, Route}
    from 'react-router-dom';

import Home from "./Home";
import Tickets from "./Tickets";
import ContactUs from "./ContactUs";

import './App.css';

function App() {
  return (
    <>
    <div><NavBar /></div>
    <SideBar side="left"></SideBar>
    <Router>
      <Routes>
          <Route exact path='/' element={<Home />} />
          <Route path='/tickets' element={<Tickets />} />
          <Route path='/contact' element={<ContactUs/>} />
      </Routes>
    </Router>
    <SideBar side="right"></SideBar>
    </>
  );
}

export default App;
