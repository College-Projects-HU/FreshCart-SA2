import { Routes } from '@angular/router'
import { authGuard } from './core/auth/guards/auth-guard';
import { adminGuard } from './core/auth/guards/admin-guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./layouts/user-layout/user-layout.component').then(m => m.UserLayoutComponent),
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      {
        path: 'home',
        loadComponent: () =>
          import('./features/home/home.component').then(m => m.HomeComponent),
        title: 'Home'
      },
      {
        path: 'shop',
        loadComponent: () =>
          import('./features/shop/shop.component').then(m => m.ShopComponent),
        title: 'Shop'
      },
      {
        path: 'categories',
        loadComponent: () =>
          import('./features/categories/categories.component').then(m => m.CategoriesComponent),
      },
      {
        path: 'brands',
        loadComponent: () =>
          import('./features/brands/brands.component').then(m => m.BrandsComponent),
      },
      {
        path: 'subcategory/:id',
        loadComponent: () =>
          import('./features/subcategory/subcategory.component').then(m => m.SubcategoryComponent),
      },
      {
        path: 'wishlist',
        loadComponent: () =>
          import('./features/wishlist/wishlist.component').then(m => m.WishlistComponent),
        canActivate: [authGuard]
      },
      {
        path: 'change',
        loadComponent: () =>
          import('./features/change-password/change-password.component').then(m => m.ChangePasswordComponent),
        canActivate: [authGuard]
      },
      {
        path: 'cart',
        loadComponent: () =>
          import('./features/cart/cart.component').then(m => m.CartComponent),
        canActivate: [authGuard]
      },
      {
        path: 'details/:id/:slug',
        loadComponent: () =>
          import('./features/details/details.component').then(m => m.DetailsComponent),
      },
      {
        path: 'checkout/:id',
        loadComponent: () =>
          import('./features/checkout/checkout.component').then(m => m.CheckoutComponent),
        canActivate: [authGuard]
      },
      {
        path: 'contact',
        loadComponent: () =>
          import('./features/contact/contact.component').then(m => m.ContactComponent),
      },
      {
        path: 'allorders',
        loadComponent: () =>
          import('./features/orders/orders.component').then(m => m.OrdersComponent),
        canActivate: [authGuard]
      },
      {
        path: 'login',
        loadComponent: () =>
          import('./features/login/login.component').then(m => m.LoginComponent),
      },
      {
        path: 'register',
        loadComponent: () =>
          import('./features/register/register.component').then(m => m.RegisterComponent),
      },
      {
        path: 'forgot',
        loadComponent: () =>
          import('./features/forgot/forgot.component').then(m => m.ForgotComponent),
      }
    ]
  },
  {
    path: '',
    loadComponent: () =>
      import('./layouts/admin-layout/admin-layout.component').then(m => m.AdminLayoutComponent),
    canActivate: [adminGuard], 
    children: [
      {
        path: 'products',
        loadComponent: () =>
          import('./features/products/products.component').then(m => m.ProductsComponent),
        title: 'Product Dashboard',
        canActivate: [adminGuard]
      },
      {
        path: 'addproduct',
        loadComponent: () =>
          import('./features/add-product/add-product.component').then(m => m.AddProductComponent),
        title: 'Add Product Dashboard',
        canActivate: [adminGuard]
      },
      {
        path: 'user',
        loadComponent: () =>
          import('./features/useres/useres.component').then(m => m.UseresComponent),
        title: 'Users Dashboard',
        canActivate: [adminGuard]
      },
      {
        path: 'content',
        loadComponent: () =>
          import('./features/content/content.component').then(m => m.ContentComponent),
        title: 'Users Dashboard',
        canActivate: [adminGuard]
      },
      {
        path: 'list',
        loadComponent: () =>
          import('./features/list-orders/list-orders.component').then(m => m.ListOrdersComponent),
        title: 'List Order Dashboard',
        canActivate: [adminGuard]
      },
    ]
  },
  {
    path: '**',
    loadComponent: () =>
      import('./features/notfound/notfound.component').then(m => m.NotfoundComponent),
  }
];