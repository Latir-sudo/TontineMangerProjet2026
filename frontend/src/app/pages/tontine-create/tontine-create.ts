import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-tontine-create',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './tontine-create.html',
  styleUrls: ['./tontine-create.scss']
})
export class TontineCreate {}