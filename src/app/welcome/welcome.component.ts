import { Component, OnInit } from '@angular/core';
import { ApiCallerService } from '../services/api-caller.service';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-welcome',
  providers: [ ApiCallerService, AuthService ],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  tweets: [{}];
  sentiment: string;
  errorMsg: string;

  constructor(private _api: ApiCallerService, private auth: AuthService, private router: Router) { }

  ngOnInit() {
    if(this.auth.getCurrentUser() == null || this.auth.getToken() == "") {
      this.router.navigate(['/login']);
    }
  }

  onSelectChange() {
    this._api.doAuthorizedPost("/tweet/get",{"sentiment": this.sentiment}).subscribe(
      res => {
        if(res.error == null) {
          console.log(res.data.tweets);
          this.tweets = res.data.tweets;
        } else {
          console.log(res.error);
          this.errorMsg = res.error.message;
        }
      }
    )
  }

  clearFilters() {
    this.sentiment = "";
  }

}
