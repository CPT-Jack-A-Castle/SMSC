import {Component, Input, HostBinding, HostListener, Renderer, ElementRef} from "@angular/core";
import {DashboardBox, Width, Height} from "./dashboard-box.model";
import {DashboardBoxService} from "./dashboard-box.service";
import {DashboardBoxTypeService} from "../dashboard-box-type/dashboard-box-type.service";
import {DashboardBoxType, Kind} from "../dashboard-box-type/dashboard-box-type.model";
import {CHART_DATA} from "./chart-data";

@Component({
    selector: 'dashboard-box',
    templateUrl: 'dashboard-box.component.html',
    styleUrls: ['dashboard-box.component.scss']
})
export class DashboardBoxComponent {

    @Input('dashboardBox') public dashboardBox: DashboardBox;

    @HostBinding('class.fullscreen') public fullscreen: boolean = false;

    @HostBinding('class') public hostClasses = 'col-lg-3 col-md-3 col-sm-6 col-xs-12';

    public isSettings: boolean = false;

    public dashboardBoxType: DashboardBoxType = <DashboardBoxType>{};

    public chartData = CHART_DATA;

    constructor(public dashboardBoxService: DashboardBoxService,
                public renderer: Renderer,
                public element: ElementRef,
                public dashboardBoxTypeService: DashboardBoxTypeService) {
    }

    ngOnInit() {
        this.dashboardBoxTypeService.getDashboardBoxType(this.dashboardBox)
            .subscribe((_dashboardBoxType: DashboardBoxType) => {
                this.dashboardBoxType = _dashboardBoxType;
            });

        this.widthChange(<Width>(this.dashboardBox.width));
        this.heightChange(<Height>(this.dashboardBox.height));
    }

    onFullscreenMode() {
        this.fullscreen = !this.fullscreen;
    }

    toggleSettings() {
        this.isSettings = !this.isSettings;
    }

    onHeightChange(event) {
        this.heightChange(event.value);
        this.dashboardBoxService.updateResource(this.dashboardBox).subscribe();
    }

    onWidthChange(event) {
        this.widthChange(event.value);
        this.dashboardBoxService.updateResource(this.dashboardBox).subscribe();
    }

    widthChange(width: Width) {
        switch (String(width)) {
            case 'WIDTH_25':
                this.hostClasses = 'col-lg-3 col-md-3 col-sm-6 col-xs-12';
                break;
            case 'WIDTH_50':
                this.hostClasses = 'col-lg-6 col-md-6 col-sm-6 col-xs-12';
                break;
            case 'WIDTH_75':
                this.hostClasses = 'col-lg-9 col-md-9 col-sm-6 col-xs-12';
                break;
            case 'WIDTH_100':
                this.hostClasses = 'col-lg-12 col-md-12 col-sm-6 col-xs-12';
                break;
        }
    }

    heightChange(height: Height) {
        switch (String(height)) {
            case 'HEIGHT_25':
                this.renderer.setElementStyle(this.element.nativeElement, 'height', '146px');
                break;
            case 'HEIGHT_50':
                this.renderer.setElementStyle(this.element.nativeElement, 'height', '312px');
                break;
            case 'HEIGHT_75':
                this.renderer.setElementStyle(this.element.nativeElement, 'height', '476px');
                break;
            case 'HEIGHT_100':
                this.renderer.setElementStyle(this.element.nativeElement, 'height', '644px');
                break;
        }
    }

    getKindOfChart(kind: Kind): string {
        switch (String(kind)) {
            case 'PIE_CHART':
                return 'pie';
            case 'SERIAL_CHART':
                return 'doughnut';
            case 'LINE_CHART':
                return 'line';
            case 'BAR_CHART':
                return 'bar';
            case 'BUBBLE_CHART':
                return 'bubble';
            default:
                return null;
        }
    }

    @HostListener('document:keydown', ['$event'])
    onKeyboard(event: KeyboardEvent) {
        if (event.code === 'Escape' && this.fullscreen) {
            this.onFullscreenMode();
        }
    }
}
