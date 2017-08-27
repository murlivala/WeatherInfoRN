import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  NativeModules,
  Button
} from 'react-native';

module.exports = NativeModules.UIManagerModule;

class BrisbaneWeatherRN extends React.Component {
	
constructor(props) {
        super(props);

        this.state = {
          weatherState : '',
          visibility : '',
          humidity : '',
          maxTemp : '',
          minTemp : '',	
		  avgTemp : '',
          createdOn : '',
          applicableOn : '',
		  windSpeed: '',
		  windDirection: '',
		  airPressure: '',
		  predictability: '',
          btnTxt: 'Press to update weather info from Native',		  
        };
      }
  render() {
	    
    return (
      <View style={styles.container}>
	    <Text style={styles.heading}>React Native View</Text>
	    <Button title={this.state.btnTxt} onPress={this.updateText}/>
	    <Text style={styles.hello}>{'Created On: '+this.state.createdOn}</Text>
		<Text style={styles.hello}>{'Applicable On: '+this.state.applicableOn}</Text>
        <Text style={styles.hello}>{'Weather State: '+this.state.weatherState}</Text>
		<Text style={styles.hello}>{'Visibility: '+this.state.visibility}</Text>
		<Text style={styles.hello}>{'Humidity: '+this.state.humidity}</Text>
		<Text style={styles.hello}>{'Max temp: '+this.state.maxTemp}</Text>
		<Text style={styles.hello}>{'Min temp: '+this.state.minTemp}</Text>
		<Text style={styles.hello}>{'Avg temp: '+this.state.avgTemp}</Text>
		<Text style={styles.hello}>{'Wind speed: '+this.state.windSpeed}</Text>
		<Text style={styles.hello}>{'Wind direction: '+this.state.windDirection}</Text>
		<Text style={styles.hello}>{'Air pressure: '+this.state.airPressure}</Text>
		<Text style={styles.hello}>{'Predictability: '+this.state.predictability}</Text>
      </View>
    )
  }
  updateText = () => {
      NativeModules.UIManagerModule.update((aWeatherState,
	                                        aVisibility,
											aHumidity,
											aMaxTemp,
											aMinTemp,
											aCreatedOn,
											aApplicableOn,
											aAvgTemp,
											aWindSpeed,
											aWindDirection,
											aAirPressure,
											aPredictability)=> { 
	  this.setState({
		  weatherState: aWeatherState,
		  visibility: aVisibility,
		  humidity: aHumidity,
		  maxTemp: aMaxTemp,
		  minTemp: aMinTemp,
		  createdOn: aCreatedOn,
		  applicableOn: aApplicableOn,
		  avgTemp: aAvgTemp,
		  windSpeed: aWindSpeed,
		  windDirection: aWindDirection,
		  airPressure: aAirPressure,
		  predictability: aPredictability,
		  btnTxt: 'Bribane yesterday\'s weather info'}) });		  
  }
		
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  heading: {
    fontSize: 24,
    textAlign: 'center',
    margin: 10,
  },
  hello: {
    fontSize: 16,
    textAlign: 'left',
    margin: 5,
  },
});

AppRegistry.registerComponent('WeatherInfoRN', () => BrisbaneWeatherRN);
