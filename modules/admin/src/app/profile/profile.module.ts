import {NgModule} from "@angular/core";
import {HTTP_INTERCEPTOR_PROVIDER} from "../shared/http-interceptor";
import {ProfileComponent} from "./profile.component";
import {ProfileService} from "./profile.service";
import {ProfileRoutingModule} from "./profile-routing.module";
import {LoadingRouterOutletModule} from "../shared/components/loading-router-outlet/loading-router-outlet.component";
import {InputTextModule} from "primeng/components/inputtext/inputtext";
import {TranslateModule} from "ng2-translate";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {MessagesModule} from "primeng/components/messages/messages";
import {ButtonModule} from "primeng/components/button/button";
import {ProfileResolve} from "./profile.resolve";
import {CustomersFormModule} from "../customers/customers-form/customers-form.component";
import {RouterModule} from "@angular/router";
import {PanelModule} from "primeng/components/panel/panel";
import {ControlErrorsModule} from "../shared/components/control-errors/control-errors.component";
import {UsersModule} from "../users/users.module";
import {SharedModule} from "primeng/components/common/shared";

@NgModule({
    imports: [
        LoadingRouterOutletModule,
        ProfileRoutingModule,
        ButtonModule,
        SharedModule,
        MessagesModule,
        FormsModule,
        CommonModule,
        TranslateModule,
        InputTextModule,
        CustomersFormModule,
        RouterModule,
        PanelModule,
        ControlErrorsModule,
        UsersModule
    ],
    exports: [
        ProfileComponent
    ],
    declarations: [
        ProfileComponent
    ],
    providers: [
        HTTP_INTERCEPTOR_PROVIDER,
        ProfileResolve
    ]
})
export class ProfileModule {
}
