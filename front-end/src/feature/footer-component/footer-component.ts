import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import {
  LucideAngularModule,
  Github,
  Linkedin,
  Twitter,
} from 'lucide-angular';

@Component({
  selector: 'app-footer-component',
  standalone: true,
  imports: [LucideAngularModule, RouterLink],
  templateUrl: './footer-component.html',
  styleUrl: './footer-component.css',
})
export class FooterComponent {
  readonly GithubIcon = Github;
  readonly LinkedinIcon = Linkedin;
  readonly TwitterIcon = Twitter;
  readonly currentYear = new Date().getFullYear();
}
