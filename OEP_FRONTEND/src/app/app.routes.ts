import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { AdminPageComponent } from './components/admin-page/admin-page.component';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { ExaminerPageComponent } from './components/examiner-page/examiner-page.component';

export const routes: Routes = [
    { path: '', component:LandingPageComponent },
    { path: 'login', component: LoginComponent },
    { path: 'admin', component: AdminPageComponent },
    { path: 'examiner', component: ExaminerPageComponent}
];
