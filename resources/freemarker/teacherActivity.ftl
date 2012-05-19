<?xml version="1.0" encoding="UTF-8"?>
<anychart>
	<settings>
		<animation enabled="true" />
	</settings>
	<charts>
		<chart plot_type="Funnel">
			<data_plot_settings enable_3d_mode="true">
				<funnel_series fit_aspect="1" mode="Square">
					<animation enabled="true" type="Appear" show_mode="Smoothed"
						start_time="0.3" duration="1.3" interpolation_type="Cubic" />
					<connector enabled="true" color="Black" opacity="0.4" />
					<label_settings enabled="true" placement_mode=""
						padding="20">
						<animation enabled="true" type="Appear" show_mode="Smoothed"
							start_time="0.3" duration="1.3" interpolation_type="Cubic" />
						<position anchor="center" padding="50" />
						<format>{%Name} - {%YValue}{numDecimals:0}</format>
						<background enabled="true">
							<inside_margin left="8" right="8" />
							<corners type="Rounded" all="3" />
						</background>
						<states>
							<hover>
								<background>
									<border type="Solid" color="DarkColor(%Color)"
										thickness="2" />
								</background>
							</hover>
							<pushed>
								<background>
									<border type="Solid" color="#494949" thickness="2"
										opacity="0.7" />
								</background>
							</pushed>
							<selected_hover>
								<background>
									<border type="Solid" color="DarkColor(%Color)"
										thickness="2" opacity="0.7" />
								</background>
							</selected_hover>
							<selected_normal>
								<background>
									<border type="Solid" color="DarkColor(%Color)"
										thickness="2" />
								</background>
							</selected_normal>
						</states>
					</label_settings>
					<tooltip_settings enabled="true">
						<format>{%Name} - {%YValue}{numDecimals:0}</format>
					</tooltip_settings>
					<funnel_style>
						<border color="Black" opacity="0.05" />
						<states>
							<hover>
								<fill color="%Color" />
								<hatch_fill enabled="true" type="Percent50" color="White"
									opacity="0.3" />
							</hover>
							<selected_hover>
								<fill color="%Color" />
								<hatch_fill type="Checkerboard" color="#404040"
									opacity="0.1" />
							</selected_hover>
							<selected_normal>
								<fill color="%Color" />
								<hatch_fill type="Checkerboard" color="Black"
									opacity="0.1" />
							</selected_normal>
						</states>
					</funnel_style>
					<marker_settings enabled="true">
						<marker type="None" anchor="Center" v_align="Center"
							h_align="Center" size="12" />
						<fill color="Yellow" />
						<border color="DarkColor(Yellow)" />
						<states>
							<hover>
								<marker type="Star5" />
							</hover>
							<pushed>
								<marker type="Star5" size="8" />
							</pushed>
							<selected_hover>
								<marker type="Star5" size="14" />
							</selected_hover>
							<selected_normal>
								<marker type="Star5" />
							</selected_normal>
						</states>
					</marker_settings>
				</funnel_series>
			</data_plot_settings>
			<chart_settings>
				<title enabled="true">
					<text>${chartName}</text>
				</title>
				<data_plot_background enabled="true">
					<inside_margin all="15" />
				</data_plot_background>
				<legend enabled="false" inside_dataplot="true"
					ignore_auto_item="true">
					<title enabled="false" />
					<items>
						<item source="Points" />
					</items>
				</legend>
			</chart_settings>
			<data>
				<series>
					<#list chartProperties as cp>
					<point name="${cp[0]}" y="${cp[1]}">
						<attributes>
							<attribute name="fullName">${cp[0]}</attribute>
							<attribute name="methodName">${methodName}</attribute>
						</attributes>
					</point>
					</#list>
				</series>
			</data>
		</chart>
	</charts>
</anychart>