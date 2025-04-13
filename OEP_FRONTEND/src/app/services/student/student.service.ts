import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Exam } from '../../model/interfaces/exam';
import { User } from '../../model/interfaces/user';

interface ApiResponse<T> {
  success: boolean;
  data?: T;
  errorMessage?: string;
  
}

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  http = inject(HttpClient);

  private baseUrl = 'http://localhost:8090/student';

  constructor() { }

  getExams(): Observable<ApiResponse<Exam[]>> {
    return this.http.get<ApiResponse<Exam[]>>(`${this.baseUrl}/getExams`);
  }

  getProfile(username: string): Observable<ApiResponse<User>> {
    const params = new HttpParams().set('username', username);
    return this.http.get<ApiResponse<User>>(`${this.baseUrl}/profile`, { params });
  }

}
