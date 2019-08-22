import { AuthService } from './../services/auth.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-navbar',
  providers: [ AuthService ],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  username: string;

  constructor(private auth: AuthService) { }

  ngOnInit() {
    this.username = this.auth.getCurrentUser().replace("@gmail.com","");
  }

}
