import { Component, OnInit } from '@angular/core';
import { ApiCallerService } from '../services/api-caller.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  providers: [ ApiCallerService ],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  fname: string;
  lname: string;
  email: string;
  pwd: string;
  otp: string;
  errorMsg: string;
  savedUsername: string;

  registerDisabled: boolean = false;

  constructor(private _api: ApiCallerService, private router: Router) { }

  ngOnInit() {
  }

  registerUser() {
    if(this.fname.trim() != "" && this.lname.trim() != "" && this.email.trim() != "" && this.pwd.trim() != "") {
      this.registerDisabled = true;
      this._api.doPostRequest("/user/signup", {'first_name':this.fname, 'last_name':this.lname
        ,'username':this.email,'password':this.pwd}).subscribe(
          res => {
            if(res.error == null) {
              console.log(res.data);
              this.savedUsername = this.email;
              this.fname = "";
              this.lname = "";
              this.email = "";
              this.pwd = "";
              let element: HTMLElement = document.getElementById('verifyOtp') as HTMLElement;
              element.click();
            } else {
              this.errorMsg = res.error.message;
            }
          }
        );
    } else {
      this.errorMsg = "Please fill up the form first.";
    }
    this.registerDisabled = false;
  }

  verifyOtp() {
    if(this.savedUsername != null && this.otp != null) {
      var url = "/user/verify_otp";
      this._api.doPostRequest(url, {'username': this.savedUsername, 'otp': this.otp}).subscribe(
        res => {
          let elem: HTMLElement = document.getElementById('dismissModal') as HTMLElement;
          console.log(res);
          if(res.error == null) {
            elem.click();
            this.router.navigate(['/login']);
          } else {
            this.errorMsg = "OTP Verification failed";
          }
        }
      );
    }
  }

}
