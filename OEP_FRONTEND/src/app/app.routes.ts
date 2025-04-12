import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { AdminPageComponent } from './components/admin-page/admin-page.component';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { ExaminerPageComponent } from './components/examiner-page/examiner-page.component';
import { StudentPageComponent } from './components/student-page/student-page.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
    { path: '', component:LandingPageComponent },
    { path: 'login', component: LoginComponent },
    { path: 'admin', component: AdminPageComponent ,canActivate: [AuthGuard], data: { roles: ['[ADMIN]'] }},
    { path: 'examiner', component: ExaminerPageComponent, canActivate: [AuthGuard], data: { roles: ['[EXAMINER]'] }},
    { path: 'student', component: StudentPageComponent, canActivate: [AuthGuard], data: { roles: ['[STUDENT]'] }}
    
];
