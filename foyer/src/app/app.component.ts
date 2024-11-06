import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BlocService } from './bloc.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,FormsModule,CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  blocs: any[] = []; // To hold the list of blocs
  bloc: any = {}; // Object for the form data
  isEditing: boolean = false; // Flag to determine if the form is in edit mode

  constructor(private blocService: BlocService) {}

  ngOnInit(): void {
    this.getBlocs(); // Load blocs on component initialization
  }

  // Method to retrieve all blocs
  getBlocs(): void {
    this.blocService.getBlocs().subscribe((blocs) => {
      this.blocs = blocs;
    });
  }

  // Method to handle form submission for adding or modifying a bloc
  onSubmit(): void {
    if (this.isEditing) {
      this.modifyBloc();
    } else {
      this.addBloc();
    }
  }

  // Method to add a new bloc
  addBloc(): void {
    this.blocService.addBloc(this.bloc).subscribe((newBloc) => {
      this.blocs.push(newBloc); // Add new bloc to the list
      this.bloc = {}; // Reset form
    });
  }

  // Method to remove a bloc by its ID
  removeBloc(bId: number): void {
    this.blocService.removeBloc(bId).subscribe(() => {
      this.blocs = this.blocs.filter((bloc) => bloc.id !== bId); // Remove from the list
    });
  }

  // Method to prepare for editing a bloc
  editBloc(bloc: any): void {
    this.bloc = { ...bloc }; // Pre-fill the form with the bloc's current data
    this.isEditing = true; // Set the form mode to editing
  }

  // Method to update a bloc's details
  modifyBloc(): void {
    this.blocService.modifyBloc(this.bloc).subscribe((updatedBloc) => {
      const index = this.blocs.findIndex((b) => b.id === updatedBloc.id);
      if (index !== -1) {
        this.blocs[index] = updatedBloc; // Update the bloc in the list
      }
      this.resetForm(); // Reset the form
    });
  }

  // Method to reset the form after add or edit operation
  resetForm(): void {
    this.bloc = {}; // Clear the form data
    this.isEditing = false; // Set form mode to add
  }
}