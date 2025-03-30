import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { routes } from './app/app.routes'; // Import your routing setup

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes), // Provides routing
    provideHttpClient(),   // Enables HttpClient for API communication
    ...appConfig.providers // Includes custom configurations from app.config
  ],
})
.catch((err) => console.error(err));
