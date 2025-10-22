/*import { Component } from '@angular/core';
import { HeaderComponent } from '../../shared/header/header.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [HeaderComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}*/

import { Component, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { HeaderComponent } from '../../shared/header/header.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, MatInputModule, MatFormFieldModule, HeaderComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class HomeComponent {
  isDragOver = false;
  uploadedFiles: File[] = [];
  uploadMessage = '';
  isProcessing = false;
  displayedImages: { file: File; url: string; name: string }[] = [];
  selectedImageIndex: number | null = null;

  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = true;
  }

  onDragEnter(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = false;

    const files = event.dataTransfer?.files;
    if (files) {
      this.handleFiles(files);
    }
  }

  onUploadContainerClick() {
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    fileInput?.click();
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.handleFiles(input.files);
    }
  }

  private handleFiles(files: FileList) {
    if (!files || files.length === 0) {
      this.uploadMessage = 'No se seleccionaron archivos';
      return;
    }

    this.isProcessing = true;
    this.uploadMessage = 'Procesando archivos...';

    const maxFileSize = 10 * 1024 * 1024; // 10MB limit
    const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'];

    const validFiles: File[] = [];
    const invalidFiles: string[] = [];

    Array.from(files).forEach(file => {
      // Check file type
      if (!allowedTypes.includes(file.type)) {
        invalidFiles.push(`${file.name} - Tipo de archivo no válido`);
        return;
      }

      // Check file size
      if (file.size > maxFileSize) {
        invalidFiles.push(`${file.name} - Archivo demasiado grande (máx. 10MB)`);
        return;
      }

      validFiles.push(file);
    });

    // Process valid files
    if (validFiles.length > 0) {
      this.uploadedFiles = [...this.uploadedFiles, ...validFiles];
      this.processValidFiles(validFiles);
    }

    // Show results
    setTimeout(() => {
      this.isProcessing = false;

      if (validFiles.length > 0 && invalidFiles.length === 0) {
        this.uploadMessage = `${validFiles.length} imagen(es) cargada(s) exitosamente`;
      } else if (validFiles.length > 0 && invalidFiles.length > 0) {
        this.uploadMessage = `${validFiles.length} imagen(es) cargada(s), ${invalidFiles.length} archivo(s) rechazado(s)`;
      } else {
        this.uploadMessage = `Error: ${invalidFiles.join(', ')}`;
      }

      // Clear message after 5 seconds
      setTimeout(() => {
        this.uploadMessage = '';
      }, 5000);
    }, 1000);
  }

  private processValidFiles(files: File[]) {
    files.forEach(file => {
      // Create preview URLs for images
      const reader = new FileReader();
      reader.onload = (e) => {
        const url = e.target?.result as string;
        if (url) {
          // Add image to displayed images array
          this.displayedImages.push({
            file: file,
            url: url,
            name: file.name
          });

          // Limit to 6 images (2x3 grid)
          if (this.displayedImages.length > 6) {
            this.displayedImages = this.displayedImages.slice(-6);
          }
        }
      };
      reader.readAsDataURL(file);
    });
  }

  removeImage(index: number) {
    if (index >= 0 && index < this.displayedImages.length) {
      // If removing the selected image, clear selection
      if (this.selectedImageIndex === index) {
        this.selectedImageIndex = null;
      } else if (this.selectedImageIndex !== null && this.selectedImageIndex > index) {
        // Adjust selected index if removing an image before the selected one
        this.selectedImageIndex--;
      }

      this.displayedImages.splice(index, 1);
      this.uploadMessage = 'Imagen eliminada exitosamente';

      // Clear message after 3 seconds
      setTimeout(() => {
        this.uploadMessage = '';
      }, 3000);
    }
  }

  selectImage(index: number) {
    if (index >= 0 && index < this.displayedImages.length) {
      // Toggle selection - if clicking the same image, deselect it
      if (this.selectedImageIndex === index) {
        this.selectedImageIndex = null;
      } else {
        this.selectedImageIndex = index;
      }

      const selectedImage = this.displayedImages[index];
      console.log('Selected image:', selectedImage ? selectedImage.name : 'None');
    }
  }

  isImageSelected(index: number): boolean {
    return this.selectedImageIndex === index;
  }

  getSelectedImage() {
    if (this.selectedImageIndex !== null && this.selectedImageIndex < this.displayedImages.length) {
      return this.displayedImages[this.selectedImageIndex];
    }
    return null;
  }

  downloadSelectedImage() {
    const selectedImage = this.getSelectedImage();
    if (selectedImage) {
      const link = document.createElement('a');
      link.href = selectedImage.url;
      link.download = selectedImage.name;
      link.click();

      this.uploadMessage = `Descargando ${selectedImage.name}...`;
      setTimeout(() => {
        this.uploadMessage = '';
      }, 3000);
    } else {
      this.uploadMessage = 'Selecciona una imagen primero';
      setTimeout(() => {
        this.uploadMessage = '';
      }, 3000);
    }
  }

  clearAllImages() {
    this.displayedImages = [];
    this.selectedImageIndex = null;
    this.uploadMessage = 'Todas las imágenes han sido eliminadas';

    setTimeout(() => {
      this.uploadMessage = '';
    }, 3000);
  }
}
