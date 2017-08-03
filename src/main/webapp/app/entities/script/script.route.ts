import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ScriptComponent } from './script.component';
import { ScriptDetailComponent } from './script-detail.component';
import { ScriptPopupComponent } from './script-dialog.component';
import { ScriptDeletePopupComponent } from './script-delete-dialog.component';

@Injectable()
export class ScriptResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const scriptRoute: Routes = [
    {
        path: 'script',
        component: ScriptComponent,
        resolve: {
            'pagingParams': ScriptResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'scriptengineApiApp.script.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'script/:id',
        component: ScriptDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'scriptengineApiApp.script.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const scriptPopupRoute: Routes = [
    {
        path: 'script-new',
        component: ScriptPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'scriptengineApiApp.script.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'script/:id/edit',
        component: ScriptPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'scriptengineApiApp.script.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'script/:id/delete',
        component: ScriptDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'scriptengineApiApp.script.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
