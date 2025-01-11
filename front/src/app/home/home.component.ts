import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';  // Assurez-vous d'importer correctement ApiService


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {
  helloMessage: string = '';  // Variable pour stocker le message retourné par l'API

  constructor(private apiService: ApiService) {}  // Injecter ApiService

  ngOnInit(): void {
    // Appeler la méthode du service pour récupérer les données
    this.apiService.getHelloMessage().subscribe(
      (response) => {
        console.log('Réponse JSON :', response);
        this.helloMessage = response.message; // Récupérer la propriété `message`
      },
      (error) => {
        console.error('Erreur :', error);
      }
    );

  }
}
