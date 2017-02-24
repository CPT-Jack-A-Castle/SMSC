import {Component} from "@angular/core";
import {TranslateService} from "ng2-translate/ng2-translate";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {NotificationService} from "../../services/notification-service";
import {CustomersService} from "../customer.service";
import {Customer} from "../model/customer";

@Component({
    selector: 'customers-create',
    template: `
        <customers-form [submitButtonName]="submitButtonName"
                    (onSubmit)="onSubmit($event)"></customers-form>
    `,
    providers: [Location]
})

export class CustomersCreateComponent {
    public submitButtonName: string = 'customers.create';

    constructor(public translate: TranslateService,
                public customersService: CustomersService,
                public route: ActivatedRoute,
                public router: Router,
                public location: Location,
                public notifications: NotificationService) {
    }

    onSubmit(data) {
        this.customersService.createResource(data)
            .subscribe((customer: Customer) => {
                this.notifications.createNotification('success', 'SUCCESS', 'customers.successCreateCustomer');

                this.router.navigate(['/customers', customer['id'], 'update']).then();
            }, err => {
                console.error(err);
                this.notifications.createNotification('error', 'ERROR', 'customers.errorCreateCustomer');
            })
    }

}