export interface Product {
  _id: string;
  id: string;
  title: string;
  description: string;
  imageCover: string;
  images: string[];
  price: number;
  quantity: number;
  sold: number;
  ratingsAverage: number;
  ratingsQuantity: number;
  slug: string;
  createdAt: string;
  updatedAt: string;
  brand: Brand;
  category: Category;
  subcategory: SubCategory[];
}

export interface Brand {
  _id: string;
  name: string;
  slug: string;
  image: string;
}

export interface Category {
  _id: string;
  name: string;
  slug: string;
  image: string;
}

export interface SubCategory {
  _id: string;
  name: string;
  slug: string;
  category: string;
}