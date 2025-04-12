import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-admin-page',
  imports: [CommonModule],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css'
})
export class AdminPageComponent {
  constructor(private authService: AuthService, private router: Router) { }
  
  selectedOption: string | null = null;

  selectOption(option: string): void {
    this.selectedOption = option;
    // You can add logic here to load data or navigate based on the selected option
    console.log('Selected option:', option);
  }

  logout(): void {
    this.authService.removeToken(); // Remove the token from localStorage
    this.router.navigate(['/login']); // Redirect the user to the login page
  }
}
