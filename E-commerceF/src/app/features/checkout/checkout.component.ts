import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/auth/services/auth.service';
import { CartService } from '../../core/services/cart.service';

@Component({
  selector: 'app-checkout',
  imports: [RouterLink , ReactiveFormsModule],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css',
})
export class CheckoutComponent implements OnInit {
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly cartService = inject(CartService)
  private readonly router = inject(Router)
  private readonly fb = inject(FormBuilder);
  flag = signal<string>("cash")
  checkOut : FormGroup = this.fb.group({
    shippingAddress : this.fb.group({
      details : ['' , [Validators.required]],
      phone : ['' , [Validators.required]],
      city : ['' , [Validators.required]]
    })
  })

  cartId = signal<string>('');
  
  ngOnInit(): void {
    this.getCartId();
  }

  getCartId():void{
    this.activatedRoute.paramMap.subscribe((params)=>{
      this.cartId.set(params.get('id')!);
    })
  }

  submitForm():void{
    
    if(this.checkOut.valid){
      if(this.flag() === 'cash'){
        this.cartService.createCashOrder(this.cartId() , this.checkOut.value).subscribe({
          next:(res)=> {
            if(res.status === 'success'){
                  this.router.navigate(['/allorders']);
                  
            }
          },
        })
      }else{
        this.cartService.createVisaOrder(this.cartId() , this.checkOut.value).subscribe({
          next:(res)=> {
            if(res.status === 'success'){
                  window.open(res.session.url , '_self')
            }
          },
        })
      }

    }
  }

  changeFlag(el:HTMLInputElement):void{
    this.flag.set(el.value)
  }

}
function computed(arg0: () => number) {
  throw new Error('Function not implemented.');
}

