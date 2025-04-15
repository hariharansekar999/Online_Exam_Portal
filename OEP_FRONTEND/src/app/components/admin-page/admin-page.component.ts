import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { AdminService } from '../../services/admin/admin-service.service';
import { ExaminerService } from '../../services/examiner/examiner.service';
import { User } from '../../model/interfaces/user';
import { Exam } from '../../model/interfaces/exam';
import { FormsModule } from '@angular/forms';
import { Report } from '../../model/interfaces/report'; // Import Report interface
import { StudentService } from '../../services/student/student.service';


interface ApiResponse<T> {
  success: boolean;
  string?: string;
  data?: T | null;
  errorMessage: string | null;
}

interface UserRequest {
  userName: string;
  email: string;
  role: 'EXAMINER' | 'ADMIN' | 'STUDENT';
  password: string;
}

interface LeaderboardEntry {
  id: number;
  examId: number;
  username: string;
  marks: number;
  position: number;
}

@Component({
  selector: 'app-admin-page',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css'
})
export class AdminPageComponent implements OnInit {
  users: User[] = [];
  exams: Exam[] = [];
  reports: Report[] = [];
  selectedOption: string | null = null;

  selectedExamId: number | null = null;
  leaderboard: LeaderboardEntry[] = [];

  loggedInUsername: string = ''; // To store the logged-in username
    userProfile: User | null = null;
  
    showPasswordForm: boolean = false;
    newPassword: string = '';
    confirmPassword: string = '';

  registrationData: UserRequest = {
    userName: '',
    email: '',
    role: 'STUDENT',
    password: '',
  };
  registrationSuccessMessage: string = '';
  registrationErrorMessage: string = '';
  showSuccessPopup: boolean = false;


  constructor(
    private authService: AuthService,
    private router: Router,
    private adminService: AdminService,
    private examinerService: ExaminerService, 
    private studentService: StudentService
  ) { }

  ngOnInit(): void {
    // Load initial data or set default view if needed
    this.selectOption(null); // Or a default option like 'users'
    this.loadExams();
    this.loadLoggedInUsername();
    this.loadUserProfile();
  }

  selectOption(option: string | null): void {
    this.selectedOption = option;
    console.log('Selected option:', option);

    if (this.selectedOption === 'users') {
      this.loadUsers();
      this.showPasswordForm = false;
    } else if (this.selectedOption === 'exams') {
      this.loadExams();
      this.showPasswordForm = false;
    } else if (this.selectedOption === 'reports') {
      this.loadReports();
      this.showPasswordForm = false;  
    } else if (this.selectedOption === 'profileUpdate') {
      console.log('Profile update selected');
    } else if (this.selectedOption === 'leaderboard') {
      this.loadLeaderboard();
      this.showPasswordForm = false;
    } else if ( this.selectedOption === 'profile') {
      this.loadLoggedInUsername();
      this.loadUserProfile();
    }
  }

  loadLeaderboard(): void {
    if (this.selectedExamId) {
      this.studentService.getLeaderboard(this.selectedExamId).subscribe( // Use StudentService
        (data) => {
          this.leaderboard = data;
        },
        (error) => {
          console.error('Error loading leaderboard:', error);
          this.leaderboard = [];
        }
      );
    }
  }

  loadUsers(): void {
    this.adminService.getAllUsers().subscribe(
      (data) => {
        this.users = data;
        console.log('Users loaded:', this.users);
      },
      (error) => {
        console.error('Error loading users:', error);
      }
    );
  }

  loadExams(): void {
    this.examinerService.getAllExams().subscribe(
      (data) => {
        this.exams = data;
        console.log('Exams loaded:', this.exams);
      },
      (error) => {
        console.error('Error loading exams:', error);
      }
    );
  }

  loadReports(): void {
    this.adminService.getAllReports().subscribe(
      (data) => {
        this.reports = data;
        console.log('Reports loaded:', this.reports);
      },
      (error) => {
        console.error('Error loading reports:', error);
      }
    );
  }


  registerUser(): void {
    this.registrationSuccessMessage = '';
    this.registrationErrorMessage = '';

    this.authService.registerUser(this.registrationData).subscribe(
      (response) => {
        console.log('Registration successful:', response);
        this.showSuccessPopup = true;
        setTimeout(() => {
          this.registrationData = { userName: '', email: '', role: 'STUDENT', password: '' };
          this.showSuccessPopup = false;
        }, 2000);
      },
      (error) => {
        console.error('Registration failed:', error);
        this.registrationErrorMessage = error?.error?.message || 'Registration failed. Please try again.';
      }
    );
  }

  deleteUser(username: string): void {
    if (confirm(`Are you sure you want to delete user "${username}"?`)) {
      this.adminService.deleteUser(username).subscribe(
        (response) => {
          console.log('User deleted successfully:', response);
          this.loadUsers();
          alert(`User "${username}" deleted successfully.`);
        },
        (error) => {
          console.error('Error deleting user:', error);
          alert(`Error deleting user "${username}". Please try again.`);
        }
      );
    }
  }

  logout(): void {
    this.authService.removeToken();
    this.router.navigate(['/login']);
  }


  
    showUpdatePasswordForm(): void {
      this.showPasswordForm = true;
    }
  
    oldPassword: string = '';
    closePasswordForm(): void {
      this.showPasswordForm = false;
      this.oldPassword = ''; // Reset oldPassword
      this.newPassword = '';
      this.confirmPassword = '';
    }
  
    loadUserProfile(): void {
      if (this.loggedInUsername) {
        this.studentService.getProfile(this.loggedInUsername).subscribe({
          next: (response) => {
            const typedResponse = response as ApiResponse<User>; // Explicit cast
            if (typedResponse.success && typedResponse.data) {
              this.userProfile = typedResponse.data;
              console.log('User profile loaded:', this.userProfile);
            } else {
              console.error('Error loading user profile:', typedResponse.errorMessage);
            }
          },
          error: (error) => {
            console.error('Error loading user profile:', error);
          }
        });
      } else {
        console.warn('Username not found, cannot load profile.');
      }
    }
  
    updatePassword(): void {
      if (this.newPassword !== this.confirmPassword) {
        alert('Passwords do not match.');
        return;
      }
  
      if (!this.loggedInUsername) {
        alert('User not logged in.');
        return;
      }
  
      // Cast the role to the correct literal type
      const role = this.userProfile?.roles[0] || 'EXAMINER';
      const updateRequest = {
        userName: this.loggedInUsername,
        email: this.userProfile?.email || '',
        role: role as 'STUDENT' | 'EXAMINER' | 'ADMIN',
        password: this.oldPassword, // Include oldPassword
      };
  
      this.authService.updatePassword(updateRequest, this.newPassword).subscribe(
        (response) => {
          alert('Password updated successfully.');
          this.closePasswordForm();
        },
        (error) => {
          console.error('Error updating password:', error);
          alert('Error updating password. Please try again.');
        }
      );
    }
  
    loadLoggedInUsername(): void {
        const token = this.authService.getToken();
        if (token) {
          const decodedToken: any = JSON.parse(atob(token.split('.')[1]));
          this.loggedInUsername = decodedToken?.sub || '';
        }
        console.log('Logged-in username:', this.loggedInUsername);
      }
}