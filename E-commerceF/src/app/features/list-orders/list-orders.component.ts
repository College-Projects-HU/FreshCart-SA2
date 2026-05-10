import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { AdminService } from '../../core/services/admin.service';
import { AllOrders } from '../../core/models/all-orders.interface';

@Component({
  selector: 'app-list-orders',
  imports: [],
  templateUrl: './list-orders.component.html',
  styleUrl: './list-orders.component.css',
})
export class ListOrdersComponent implements OnInit{

  private readonly adminService  = inject(AdminService);
  orderList = signal<AllOrders[]>([])
  ngOnInit(): void {
    this.getAllOrders();
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
    
    getInitials(username: string): string {
      return username.trim().split(' ').filter(Boolean).map(word => word[0].toUpperCase()).slice(0, 2).join('');
    }
    getAllOrders():void{
    this.adminService.getAllOrders().subscribe({
      next:(res)=>{
        
        this.orderList.set(res.data)
      },
      error:(err)=>{
        console.log(err)
      },
        })
  }
  searchQuery = signal<string>('');
  selectedPayment = signal<string>('');
  filteredOrders = computed(() => {
    const query = this.searchQuery().toLowerCase().trim();
    const payment = this.selectedPayment();

    return this.orderList().filter(order => {
      const matchesSearch =
        !query ||
        order.user.name.toLowerCase().includes(query) ||
        order.shippingAddress.city.toLowerCase().includes(query);
      const matchesPayment =
        !payment ||
        order.paymentMethodType.toLowerCase() === payment.toLowerCase();
      return matchesSearch && matchesPayment;
    });
  });
  
  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchQuery.set(input.value);
  }

  onPaymentFilter(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.selectedPayment.set(select.value);
  }
}
