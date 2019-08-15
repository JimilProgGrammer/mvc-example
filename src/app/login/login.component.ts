import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ApiCallerService } from '../services/api-caller.service';

@Component({
  selector: 'app-login',
  providers: [ AuthService ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  // Class members
  loginEmail: string;
  loginPwd: string;
  errorMsg: string;

  /**
   * Initializing important service instances.
   * 
   * @param router 
   */
  constructor(private router: Router, private auth: AuthService, private _api: ApiCallerService) { 
  }

  /**
   * Life-cycle hook
   * Use this to load important data needed
   * on component load.
   */
  ngOnInit() {
    if(this.auth.getToken() != null && this.auth.getToken().trim() != "") {
      this._api.doAuthorizedPost("/user/verify_token", {}).subscribe(res => {
        if(res.error == null) {
          console.log(res);
          console.log("Forwarding to welcome");
          this.router.navigate(['/welcome']);
        }
      });
    }
  }

  /**
   * Attempt login by validating login ID and pwd.
   */
  attemptLogin() {
    if(this.loginEmail != null && this.loginEmail.trim() != "" && this.loginPwd != null && this.loginPwd.trim() != "") {
      this.auth.login(this.loginEmail, this.loginPwd).subscribe(res => {
        if(res.error == null) {
          console.log(res);
          this.auth.setToken(res.data.token);
          this.auth.setCurrentUser(res.data.username);
          this.router.navigate(['/welcome']);
        } else {
          this.errorMsg = res.error.message;
        }
      });
    } else {
      this.errorMsg = "Username and Password cannot be blank strings.";
    }
  }

}