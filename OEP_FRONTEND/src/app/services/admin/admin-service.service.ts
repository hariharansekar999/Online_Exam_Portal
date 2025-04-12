import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { User } from '../../model/interfaces/user';

interface ApiResponse {
  success: boolean;
  status: string;
  data: User[];
  errorMessage: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  http = inject(HttpClient);
  private baseUrl = 'http://localhost:8090/admin';

  constructor() { }

  getAllUsers(): Observable<User[]> {
    return this.http.get<ApiResponse>(this.baseUrl+'/getAllUsers').pipe(
      map(response => {
        if (response.success && response.data) {
          console.log('Users fetched successfully:', response.data);
          return response.data;
        } else {
          console.error('Error fetching users:', response.errorMessage);
          return []; // Return an empty array in case of error
        }
      })
    );
  }
}
