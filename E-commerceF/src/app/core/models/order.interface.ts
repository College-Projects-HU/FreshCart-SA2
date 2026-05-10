
export interface Order {
    shippingAddress:   ShippingAddress;
    taxPrice:          number;
    shippingPrice:     number;
    totalOrderPrice:   number;
    paymentMethodType: string;
    isPaid:            boolean;
    isDelivered:       boolean;
    _id:               string;
    user:              User;
    cartItems:         CartItem[];
    createdAt:         Date;
    updatedAt:         Date;
    id:                number;
}

export interface CartItem {
    count:   number;
    product: Product;
    price:   number;
}

export interface Product {
    subcategory:     Brand[];
    ratingsQuantity: number;
    _id:             string;
    title:           string;
    imageCover:      string;
    category:        Brand;
    brand:           Brand;
    ratingsAverage:  number;
}

export interface Brand {
    _id:       string;
    name:      string;
    slug:      string;
    image?:    string;
    category?: string;
}

export interface ShippingAddress {
    details:    string;
    phone:      string;
    city:       string;
    postalCode: null;
}

export interface User {
    name:  string;
    email: string;
    phone: string;
}
