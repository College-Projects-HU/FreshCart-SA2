import { isPlatformBrowser } from '@angular/common';
import { Component, computed, HostListener, inject, OnDestroy, OnInit, PLATFORM_ID } from '@angular/core';
import { FlowbiteService } from '../../core/services/flowbite.service';
import { AuthService } from '../../core/auth/services/auth.service';

@Component({
  selector: 'app-header-d',
  imports: [],
  templateUrl: './header-d.component.html',
  styleUrl: './header-d.component.css',
})
export class HeaderDComponent implements OnInit  {
  sidebarOpen = false;
  private readonly authService = inject(AuthService)
  private readonly pLATFORM_ID = inject(PLATFORM_ID);
  nameU:string ="";
  emailU:string ="";
  InitN:string=""
  ngOnInit(): void {
    if (isPlatformBrowser(this.pLATFORM_ID)) {
    const userToken = localStorage.getItem('userToken');
    if (userToken) {
      try {
        const user = JSON.parse(userToken);
        this.nameU = user?.name ?? '';
        this.InitN =  this.getInitials(this.nameU)
        this.emailU = user?.email ?? '';
      } catch (e) {
        console.log('Invalid userToken');
      }
    }
  }
  }
  closeSidebar(): void {
    this.sidebarOpen = false;
    document.body.classList.remove('overflow-hidden');
  }
  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
    document.body.classList.toggle('overflow-hidden', this.sidebarOpen);
  }
  @HostListener('document:keydown.escape')
  onEsc(): void {
    this.sidebarOpen = false;
    document.body.classList.remove('overflow-hidden');
  }
  logOut():void{
    this.authService.signOut();
  }
   getInitials(username: string): string {
      return username.trim().split(' ').filter(Boolean).map(word => word[0].toUpperCase()).slice(0, 2).join('');
}

}
