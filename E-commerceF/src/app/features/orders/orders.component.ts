import { AuthService } from './../../core/auth/services/auth.service';
import { Component, inject, OnInit, signal } from '@angular/core';
import { OrdersService } from '../../core/services/orders.service';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Order } from '../../core/models/order.interface';

@Component({
  selector: 'app-orders',
  imports: [DatePipe, RouterLink],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css',
})
export class OrdersComponent implements OnInit {
  private readonly authService  = inject(AuthService);
  private readonly ordersService = inject(OrdersService);

  orderDetails = signal<Order[]>([]);
  expandedOrders = signal<Set<string>>(new Set());

  ngOnInit(): void {
    this.getUserOrder(this.authService.getCurrentUserId());
  }

  getUserOrder(id: string): void {
    this.ordersService.getUserOrders(id).subscribe({
      next: (res) => {
        this.orderDetails.set(res);
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  toggleOrder(orderId: string): void {
    this.expandedOrders.update(set => {
      const next = new Set(set);
      if (next.has(orderId)) {
        next.delete(orderId);
      } else {
        next.add(orderId);
      }
      return next;
    });
  }

  isExpanded(orderId: string): boolean {
    return this.expandedOrders().has(orderId);
  }
}