import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true, // Standalone component
  imports: [FormsModule], // Add FormsModule for ngModel
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  credentials = { username: '', password: '',role:'' };

  onSubmit() {
    console.log('Logging in with:', this.credentials);
  }
}
