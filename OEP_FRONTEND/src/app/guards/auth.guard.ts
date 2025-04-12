import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth/auth.service'; // Import your AuthService

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate():
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    if (this.authService.isAuthenticated()) {
      return true; // User is logged in, allow access to the route
    } else {
      // User is not logged in, redirect to the login page
      alert('Please log in to access this page.'); // Optional alert
      return this.router.parseUrl('/login'); // Redirect to the login route
      // Alternatively, you could navigate directly:
      // this.router.navigate(['/login']);
      // return false;
    }
  }
}