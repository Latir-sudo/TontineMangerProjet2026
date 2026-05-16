import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../services/api.service';

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
  tontineId: string | null = null;
  tontineName = '';

  constructor(private api: ApiService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.tontineId = this.route.snapshot.paramMap.get('id');
    if (this.tontineId) {
      this.loadTontineName(parseInt(this.tontineId, 10));
    }
  }

  private async loadTontineName(id: number): Promise<void> {
    try {
      const tontine = await this.api.get<any>(`/tontine/${id}`);
      this.tontineName = tontine?.nomTontine || '';
    } catch (e) {
      this.tontineName = '';
    }
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
    const userId = user.id ?? user._id ?? user.phone ?? JSON.stringify(user);
    if (this.addedIds.has(userId)) return;
    try {
      if (this.tontineId) {
        await this.api.post(`/tontine/${this.tontineId}/members`, { userId });
      } else {
        await this.api.post(`/tontine/members`, { userId });
      }
      this.addedIds.add(userId);
      alert('✅ Membre ajouté à la tontine avec succès !');
    } catch (e) {
      console.error('Erreur ajout membre:', e);
      alert('Erreur lors de l\'ajout du membre.');
    }
  }

  back(): void {
    this.router.navigate(['/tontines']);
  }
}
