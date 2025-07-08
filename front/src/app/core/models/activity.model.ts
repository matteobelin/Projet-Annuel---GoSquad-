export interface Activity {
  id: number;
  name: string;
  description: string;
  address: string;
  city: string;
  postalCode?: string;
  isoCode: string;
  country: string;
  categoryName: string;
  netPrice: number;
  vatRate: number;
  gross_price?: number;
}
