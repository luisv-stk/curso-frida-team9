// File: app.component.ts
import { Component, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';

@Component({
    selector: 'search',
    standalone: true,
    imports: [
        CommonModule,
        MatIconModule,
        MatToolbarModule,
        MatFormFieldModule,
        MatInputModule,
        FormsModule,
        MatSelectModule,
        MatButtonModule,
        MatMenuModule
    ],
    templateUrl: './search.component.html',
    styleUrls: ['./search.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class SearchComponent {
    categories = [
        {
            title: 'Naturaleza',
            images: [
                'https://placehold.co/400x400/jpg',
                'https://placehold.co/400x400/jpg',
                'https://placehold.co/400x400/jpg'
            ]
        },
        {
            title: 'Comida',
            images: [
                'https://placehold.co/400x400/jpg',
                'https://placehold.co/400x400/jpg',
                'https://placehold.co/400x400/jpg'
            ]
        },
        {
            title: 'Bebida',
            images: [
                'https://placehold.co/400x400/jpg',
                'https://placehold.co/400x400/jpg',
                'https://placehold.co/400x400/jpg'
            ]
        }
    ];
}

