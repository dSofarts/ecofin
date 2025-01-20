import {Component, inject} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {UserService} from "../../data/service/user.service";

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    RouterOutlet
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {

  userService = inject(UserService);

  ngOnInit() {
    this.userService.getMe().subscribe(res => {
      console.log(res);
      console.log(res.userInfo);
    })
  }
}
