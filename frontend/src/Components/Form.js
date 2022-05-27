import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';

const Form = () => {
  return (
    <form>
        <div class="form-group">
            <label for="firstName">First Name</label>
            <input class="form-control" required="required" type="text" id="firstName"/><br />
        </div><div class="form-group">
            <label for="firstName">Last Name</label>
            <input class="form-control" required="required" type="text" id="lastName"/><br />
        </div><div class="form-group">
            <label for="occupation">Occupation</label>
            <input class="form-control" required="required" type="text" id="occupation"/><br />
        </div><div class="form-group">
            <label for="birth">Date of Birth</label>
            <input class="form-control" required="required" type="date" id="birth"/><br />
        </div>
        <button type="submit" class="btn btn-primary">Enter into our Charity Raffle</button>
    </form>
  )
}

export default Form