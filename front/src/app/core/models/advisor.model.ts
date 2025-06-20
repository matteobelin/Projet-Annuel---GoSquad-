/**
 * Advisor Interface
 */
export interface Advisor {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
  phone?: string;
  specialization?: string;
  createdAt?: Date;
  updatedAt?: Date;
  role: string;
  companyCode: string;
}
