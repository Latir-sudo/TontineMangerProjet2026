import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../../services/api.service';

@Component({
  selector: 'app-add-member',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-member.component.html',
  styleUrls: ['./add-member.component.scss']
})
export class AddMemberComponent implements OnInit {
  query = '';
  filterBy: 'name' | 'phone' = 'name';
  results: any[] = [];
  loading = false;
  addedIds = new Set<string | number>();
  addingIds = new Set<string | number>();
  message = '';
  tontineId: string | null = null;

  constructor(private api: ApiService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.tontineId = this.route.snapshot.paramMap.get('id');
  }

  async search(): Promise<void> {
    if (!this.query.trim()) {
      this.results = [];
      return;
    }
    this.loading = true;
    try {
      const endpoint = `/users?${this.filterBy}=${encodeURIComponent(this.query.trim())}`;
      const res = await this.api.get<any[]>(endpoint).catch(() => null as any);
      if (Array.isArray(res)) {
        this.results = res;
      } else {
        this.results = [];
      }
    } catch (e) {
      this.results = [];
    } finally {
      this.loading = false;
    }
  }

  async addMember(user: any): Promise<void> {
    if (!user) return;
    const userId = this.getUserKey(user);
    if (this.addedIds.has(userId) || this.addingIds.has(userId)) return;
    this.addingIds.add(userId);
    try {
      if (this.tontineId) {
        await this.api.post(`/tontines/${this.tontineId}/members`, { userId });
      } else {
        await this.api.post(`/tontines/members`, { userId });
      }
      this.addedIds.add(userId);
      this.message = 'Membre ajouté avec succès.';
      setTimeout(() => (this.message = ''), 3000);
    } catch (e) {
      console.error('Erreur ajout membre', e);
      this.message = 'Échec de l\'ajout — réessayez.';
      setTimeout(() => (this.message = ''), 4000);
    } finally {
      this.addingIds.delete(userId);
    }
  }

  getUserKey(user: any): string | number {
    return user.id ?? user._id ?? user.phone ?? JSON.stringify(user);
  }

  back(): void {
    this.router.navigate(['/tontines']);
  }
}
