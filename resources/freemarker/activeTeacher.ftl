<?xml version="1.0" encoding="UTF-8"?>
<anychart>
	<settings>
		<animation enabled="True" />
	</settings>
	<charts>
		<chart plot_type="CategorizedHorizontal">
			<data_plot_settings default_series_type="Bar">
				<bar_series group_padding="0.2">
					<bar_style>
						<hatch_fill enabled="true" type="%HatchType" color="White"
							thickness="1" pattern_size="2" opacity="0.5" />
					</bar_style>
					<tooltip_settings enabled="true" />
				</bar_series>
			</data_plot_settings>
			<chart_settings>
				<title enabled="true">
					<text>${chartName}</text>
				</title>
				<axes>
					<y_axis position="Opposite">
						<title>
							<text>${yAxis}</text>
						</title>
					</y_axis>
					<x_axis>
						<title>
							<text>${xAxis}</text>
						</title>
					</x_axis>
				</axes>
			</chart_settings>
			<data>
				<series name="Series 1">
					<#list chartProperties as cp>
					<point y="${cp[1]}" name="${cp[0]}" hatch_type="${hatchType}">
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