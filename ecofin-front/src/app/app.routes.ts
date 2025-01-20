import {Routes} from '@angular/router';
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {RegistrationPageComponent} from "./pages/registration-page/registration-page.component";
import {LayoutComponent} from "./common-ui/layout/layout.component";
import {MainPageComponent} from "./pages/main-page/main-page.component";
import {canActivateAuth, canLoginPage} from "./auth/access.guard";

export const routes: Routes = [
  {
    path: '', component: LayoutComponent, children: [
      {path: '', component: MainPageComponent},
    ],
    canActivate: [canActivateAuth]
  },
  {path: 'login', component: LoginPageComponent, canActivate: [canLoginPage]},
  {path: 'registration', component: RegistrationPageComponent, canActivate: [canLoginPage]},
];
