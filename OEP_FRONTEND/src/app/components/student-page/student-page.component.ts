import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { StudentService } from '../../services/student/student.service'; // Import StudentService
import { Exam } from '../../model/interfaces/exam'; // Import your Exam interface
import { User } from '../../model/interfaces/user'; // Import your User interface
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface ApiResponse<T> {
  success: boolean;
  data?: T;
  errorMessage?: string;
}

@Component({
  selector: 'app-student-page',
  templateUrl: './student-page.component.html',
  styleUrls: ['./student-page.component.css'],
  imports:[RouterLink, CommonModule, FormsModule]
})
export class StudentPageComponent implements OnInit {
  selectedOption: string = 'availableExams'; // Default to show exams
  availableExams: Exam[] = [];
  loggedInUsername: string = ''; // To store the logged-in username
  userProfile: User | null = null;

  constructor(
    private router: Router,
    private authService: AuthService,
    private studentService: StudentService // Inject StudentService
  ) { }

  ngOnInit(): void {
    // this.loadLoggedInUsername();
    // this.loadUserProfile();
    // this.loadAvailableExams(); // Load exams when the page loads
  }

  selectOption(option: string): void {
    this.selectedOption = option;
    console.log(`Selected option: ${this.selectedOption}`);
    if (option === 'availableExams') {
      this.loadAvailableExams();
    } else if (option === 'attendExam') {
      console.log('Attend Exam section clicked.');
    } else if (option === 'viewReport') {
      console.log('Get Report section clicked.');
    } else if (option === 'leaderboard') {
      console.log('View Leaderboard section clicked.');
    } else if( option === 'profile') {
      console.log('Profile section clicked.');
      this.loadLoggedInUsername();
      // this.loadUserProfile();
    }
  }

  logout(): void {
    this.authService.removeToken();
    this.router.navigate(['/login']);
  }

  loadLoggedInUsername(): void {
    const token = this.authService.getToken();
    if (token) {
      const decodedToken: any = JSON.parse(atob(token.split('.')[1]));
      this.loggedInUsername = decodedToken?.sub || '';
    }
    console.log('Logged-in username:', this.loggedInUsername);
  }

  loadUserProfile(): void {
    if (this.loggedInUsername) {
      this.studentService.getProfile(this.loggedInUsername).subscribe({
        next: (response: ApiResponse<User>) => {
          if (response.success && response.data) {
            this.userProfile = response.data;
            console.log('User profile loaded:', this.userProfile);
          } else {
            console.error('Error loading user profile:', response.errorMessage);
            // Handle error appropriately
          }
        },
        error: (error) => {
          console.error('Error loading user profile:', error);
          // Handle error appropriately
        }
      });
    } else {
      console.warn('Username not found, cannot load profile.');
    }
  }

  loadAvailableExams(): void {
    this.studentService.getExams().subscribe(
      (response: ApiResponse<Exam[]>) => {
        if (response.success && response.data) {
          this.availableExams = response.data;
          console.log('Available exams loaded:', this.availableExams);
        } else {
          console.error('Error loading available exams:', response.errorMessage);
        }
      },
      (error) => {
        console.error('Error loading available exams:', error);
      }
    );
  }
}