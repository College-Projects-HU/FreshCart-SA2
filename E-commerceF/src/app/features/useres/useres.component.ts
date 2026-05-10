import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { AdminService } from '../../core/services/admin.service';
import { Product } from '../../core/models/product.interface';
import { Users } from '../../core/models/users.interface';
import { RouterLink } from "@angular/router";
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-useres',
  imports: [RouterLink],
  templateUrl: './useres.component.html',
  styleUrl: './useres.component.css',
})
export class UseresComponent implements OnInit {
  private readonly adminService = inject(AdminService);
  private readonly toastrService = inject(ToastrService);
  usersList = signal<Users[]>([])
  searchQuery = signal<string>('');
  selectedRole = signal<string>('');


  filteredUsers = computed(() => {
    const query = this.searchQuery().toLowerCase().trim();
    const role = this.selectedRole();
    return this.usersList().filter(user => {
      const matchesSearch =
        !query ||
        user.name.toLowerCase().includes(query) ||
        user.email.toLowerCase().includes(query);

      const matchesRole =
        !role ||
        user.role.toLowerCase() === role.toLowerCase();

      return matchesSearch && matchesRole;
    });
  });

  ngOnInit(): void {
    this.getUsersData();
  }

  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchQuery.set(input.value);
  }

  onRoleFilter(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.selectedRole.set(select.value);
  }
  getUsersData():void{
    this.adminService.getAllUsers().subscribe({
      next:(res)=>{
        console.log(res)
        this.usersList.set(res)
      },
      error:(err)=>{
        console.log(err)
      },
        })
  }
  getInitials(username: string): string {
      return username.trim().split(' ').filter(Boolean).map(word => word[0].toUpperCase()).slice(0, 2).join('');
  }

  formatDateToShort(dateString: string| Date): string {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    month: 'short',
    day: '2-digit',
    year: 'numeric',
  });
  }

  formatTime(dateString: string | Date): string {
  const date = new Date(dateString);
  return date.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true,
  });
  }

  deleteUser(id:string):void{
     this.adminService.deleteUser(id).subscribe({
      next:(res)=>{
        this.toastrService.success('User Removed' , 'FreshCart' , {progressBar:true , closeButton:true});
        this.usersList.update(users =>
        users.filter(user => user.id !== id)
      );
      },
    })
  }
}
