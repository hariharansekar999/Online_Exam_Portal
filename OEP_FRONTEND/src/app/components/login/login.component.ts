import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-login',
  imports: [CommonModule,FormsModule,HeaderComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  isLoading: boolean = true; // Initially set to true to show the spinner
  loginData = { username: '', password: '', role: '' };
  errorMessage: string = '';

  ngOnInit(): void {
    setTimeout(() => {
      this.isLoading = false; // Set to false after the initial page content is loaded
    }, 900); // Adjust the delay as needed
  }

  login() :void {
    this.isLoading = true; // Show the spinner when login is initiated
    // Simulate a login request
    setTimeout(() => {
      if (this.loginData.username === 'admin' && this.loginData.password === 'admin' && this.loginData.role === 'ADMIN') {
        // Successful login
        this.errorMessage = '';
        this.isLoading = false; // Hide the spinner after login
        alert('Login successful!'); // Replace with actual navigation logic
      } else {
        // Failed login
        this.errorMessage = 'Invalid username or password';
        this.isLoading = false; // Hide the spinner after login attempt
        alert(this.errorMessage); // Replace with actual error handling logic
      }
    }, 2000); // Simulate a 2-second delay for the login request
  }
  
}
