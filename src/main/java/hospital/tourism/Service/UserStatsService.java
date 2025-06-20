package hospital.tourism.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.repo.usersrepo;

@Service
public class UserStatsService {
    
    @Autowired
    private usersrepo userRepo;
    
    private Map<String, double[]> countryCoordinates;
    
    public UserStatsService() {
        initializeCountryCoordinates();
    }

    /**
     * Get user count grouped by country with coordinates for globe display
     * @return List of maps containing country name, user count, and coordinates
     */
    public List<Map<String, Object>> getUserCountByCountry() {
        // Get raw country statistics from database
        List<Object[]> rawStats = userRepo.countUsersByCountry();
        
        return rawStats.stream().map(row -> {
            String country = (String) row[0];
            Long userCount = (Long) row[1];
            
            // Get coordinates for the country
            double[] coordinates = getCountryCoordinates(country);
            
            Map<String, Object> result = new HashMap<>();
            result.put("country", country);
            result.put("userCount", userCount);
            result.put("latitude", coordinates[0]);
            result.put("longitude", coordinates[1]);
            return result;
        }).collect(Collectors.toList());
    }

    /**
     * Get total count of all users
     * @return Total user count
     */
    public Long getTotalUserCount() {
        return userRepo.count();
    }

    /**
     * Get approximate coordinates for a country (capital city coordinates)
     * @param country Country name
     * @return Array containing [latitude, longitude]
     */
    private double[] getCountryCoordinates(String country) {
        return countryCoordinates.getOrDefault(country, new double[]{0.0, 0.0});
    }
    
    private void initializeCountryCoordinates() {
        countryCoordinates = new HashMap<>();
        
        // Comprehensive mapping of countries to their capital city coordinates
        // Using exact country names from react-select-country-list
        countryCoordinates.put("Afghanistan", new double[]{34.5553, 69.2075});
        countryCoordinates.put("Åland Islands", new double[]{60.1785, 19.9156});
        countryCoordinates.put("Albania", new double[]{41.3275, 19.8187});
        countryCoordinates.put("Algeria", new double[]{36.7538, 3.0588});
        countryCoordinates.put("American Samoa", new double[]{-14.2710, -170.1322});
        countryCoordinates.put("Andorra", new double[]{42.5063, 1.5218});
        countryCoordinates.put("Angola", new double[]{-8.8390, 13.2894});
        countryCoordinates.put("Anguilla", new double[]{18.2206, -63.0686});
        countryCoordinates.put("Antarctica", new double[]{-82.8628, 135.0000});
        countryCoordinates.put("Antigua and Barbuda", new double[]{17.0608, -61.7964});
        countryCoordinates.put("Argentina", new double[]{-34.6118, -58.3960});
        countryCoordinates.put("Armenia", new double[]{40.1792, 44.4991});
        countryCoordinates.put("Aruba", new double[]{12.5211, -69.9683});
        countryCoordinates.put("Australia", new double[]{-35.2809, 149.1300});
        countryCoordinates.put("Austria", new double[]{48.2082, 16.3738});
        countryCoordinates.put("Azerbaijan", new double[]{40.4093, 49.8671});
        countryCoordinates.put("Bahamas", new double[]{25.0443, -77.3504});
        countryCoordinates.put("Bahrain", new double[]{26.0667, 50.5577});
        countryCoordinates.put("Bangladesh", new double[]{23.8103, 90.4125});
        countryCoordinates.put("Barbados", new double[]{13.1939, -59.5432});
        countryCoordinates.put("Belarus", new double[]{53.9045, 27.5615});
        countryCoordinates.put("Belgium", new double[]{50.8503, 4.3517});
        countryCoordinates.put("Belize", new double[]{17.2515, -88.7600});
        countryCoordinates.put("Benin", new double[]{6.4969, 2.6283});
        countryCoordinates.put("Bermuda", new double[]{32.3078, -64.7505});
        countryCoordinates.put("Bhutan", new double[]{27.5142, 89.6339});
        countryCoordinates.put("Bolivia, Plurinational State of", new double[]{-16.2902, -63.5887});
        countryCoordinates.put("Bonaire, Sint Eustatius and Saba", new double[]{12.1784, -68.2385});
        countryCoordinates.put("Bosnia and Herzegovina", new double[]{43.8564, 18.4131});
        countryCoordinates.put("Botswana", new double[]{-24.6282, 25.9231});
        countryCoordinates.put("Bouvet Island", new double[]{-54.4208, 3.3464});
        countryCoordinates.put("Brazil", new double[]{-15.8267, -47.9218});
        countryCoordinates.put("British Indian Ocean Territory", new double[]{-6.3432, 71.8765});
        countryCoordinates.put("Brunei Darussalam", new double[]{4.9031, 114.9398});
        countryCoordinates.put("Bulgaria", new double[]{42.6977, 23.3219});
        countryCoordinates.put("Burkina Faso", new double[]{12.3714, -1.5197});
        countryCoordinates.put("Burundi", new double[]{-3.3731, 29.9189});
        countryCoordinates.put("Cambodia", new double[]{11.5449, 104.8922});
        countryCoordinates.put("Cameroon", new double[]{3.8480, 11.5021});
        countryCoordinates.put("Canada", new double[]{45.4215, -75.6972});
        countryCoordinates.put("Cape Verde", new double[]{14.9331, -23.5133});
        countryCoordinates.put("Cayman Islands", new double[]{19.3133, -81.2546});
        countryCoordinates.put("Central African Republic", new double[]{4.3947, 18.5582});
        countryCoordinates.put("Chad", new double[]{15.4542, 18.7322});
        countryCoordinates.put("Chile", new double[]{-33.4489, -70.6693});
        countryCoordinates.put("China", new double[]{39.9042, 116.4074});
        countryCoordinates.put("Christmas Island", new double[]{-10.4475, 105.6904});
        countryCoordinates.put("Cocos (Keeling) Islands", new double[]{-12.1642, 96.8710});
        countryCoordinates.put("Colombia", new double[]{4.7110, -74.0721});
        countryCoordinates.put("Comoros", new double[]{-11.6455, 43.3333});
        countryCoordinates.put("Congo", new double[]{-4.2634, 15.2429});
        countryCoordinates.put("Congo, the Democratic Republic of the", new double[]{-4.4419, 15.2663});
        countryCoordinates.put("Cook Islands", new double[]{-21.2367, -159.7777});
        countryCoordinates.put("Costa Rica", new double[]{9.7489, -83.7534});
        countryCoordinates.put("Côte d'Ivoire", new double[]{6.8277, -5.2893});
        countryCoordinates.put("Croatia", new double[]{45.8150, 15.9819});
        countryCoordinates.put("Cuba", new double[]{23.1136, -82.3666});
        countryCoordinates.put("Curaçao", new double[]{12.1696, -68.9900});
        countryCoordinates.put("Cyprus", new double[]{35.1856, 33.3823});
        countryCoordinates.put("Czech Republic", new double[]{50.0755, 14.4378});
        countryCoordinates.put("Denmark", new double[]{55.6761, 12.5683});
        countryCoordinates.put("Djibouti", new double[]{11.8251, 42.5903});
        countryCoordinates.put("Dominica", new double[]{15.4140, -61.3710});
        countryCoordinates.put("Dominican Republic", new double[]{18.7357, -70.1627});
        countryCoordinates.put("Ecuador", new double[]{-0.1807, -78.4678});
        countryCoordinates.put("Egypt", new double[]{30.0444, 31.2357});
        countryCoordinates.put("El Salvador", new double[]{13.7942, -88.8965});
        countryCoordinates.put("Equatorial Guinea", new double[]{3.7504, 8.7371});
        countryCoordinates.put("Eritrea", new double[]{15.3229, 38.9251});
        countryCoordinates.put("Estonia", new double[]{59.4370, 24.7536});
        countryCoordinates.put("Ethiopia", new double[]{9.1450, 40.4897});
        countryCoordinates.put("Falkland Islands (Malvinas)", new double[]{-51.7963, -59.5236});
        countryCoordinates.put("Faroe Islands", new double[]{62.0079, -6.7900});
        countryCoordinates.put("Fiji", new double[]{-18.1248, 178.4501});
        countryCoordinates.put("Finland", new double[]{60.1699, 24.9384});
        countryCoordinates.put("France", new double[]{48.8566, 2.3522});
        countryCoordinates.put("French Guiana", new double[]{3.9339, -53.1258});
        countryCoordinates.put("French Polynesia", new double[]{-17.6797, -149.4068});
        countryCoordinates.put("French Southern Territories", new double[]{-49.2804, 69.3486});
        countryCoordinates.put("Gabon", new double[]{0.4162, 9.4673});
        countryCoordinates.put("Gambia", new double[]{13.4432, -15.3101});
        countryCoordinates.put("Georgia", new double[]{41.7151, 44.8271});
        countryCoordinates.put("Germany", new double[]{52.5200, 13.4050});
        countryCoordinates.put("Ghana", new double[]{5.6037, -0.1870});
        countryCoordinates.put("Gibraltar", new double[]{36.1408, -5.3536});
        countryCoordinates.put("Greece", new double[]{37.9755, 23.7348});
        countryCoordinates.put("Greenland", new double[]{64.1836, -51.7214});
        countryCoordinates.put("Grenada", new double[]{12.1165, -61.6790});
        countryCoordinates.put("Guadeloupe", new double[]{16.2650, -61.5510});
        countryCoordinates.put("Guam", new double[]{13.4443, 144.7937});
        countryCoordinates.put("Guatemala", new double[]{14.6349, -90.5069});
        countryCoordinates.put("Guernsey", new double[]{49.4484, -2.5801});
        countryCoordinates.put("Guinea", new double[]{9.6412, -13.5784});
        countryCoordinates.put("Guinea-Bissau", new double[]{11.8037, -15.1804});
        countryCoordinates.put("Guyana", new double[]{6.8013, -58.1551});
        countryCoordinates.put("Haiti", new double[]{18.9712, -72.2852});
        countryCoordinates.put("Heard Island and McDonald Islands", new double[]{-53.0818, 73.5042});
        countryCoordinates.put("Holy See (Vatican City State)", new double[]{41.9029, 12.4534});
        countryCoordinates.put("Honduras", new double[]{14.0723, -87.1921});
        countryCoordinates.put("Hong Kong", new double[]{22.3193, 114.1694});
        countryCoordinates.put("Hungary", new double[]{47.4979, 19.0402});
        countryCoordinates.put("Iceland", new double[]{64.1466, -21.9426});
        countryCoordinates.put("India", new double[]{28.6139, 77.2090});
        countryCoordinates.put("Indonesia", new double[]{-6.2088, 106.8456});
        countryCoordinates.put("Iran, Islamic Republic of", new double[]{35.6892, 51.3890});
        countryCoordinates.put("Iraq", new double[]{33.3152, 44.3661});
        countryCoordinates.put("Ireland", new double[]{53.3498, -6.2603});
        countryCoordinates.put("Isle of Man", new double[]{54.2361, -4.5481});
        countryCoordinates.put("Israel", new double[]{31.0461, 34.8516});
        countryCoordinates.put("Italy", new double[]{41.9028, 12.4964});
        countryCoordinates.put("Jamaica", new double[]{17.9971, -76.7936});
        countryCoordinates.put("Japan", new double[]{35.6762, 139.6503});
        countryCoordinates.put("Jersey", new double[]{49.1900, -2.1100});
        countryCoordinates.put("Jordan", new double[]{31.9539, 35.9106});
        countryCoordinates.put("Kazakhstan", new double[]{51.1694, 71.4491});
        countryCoordinates.put("Kenya", new double[]{-1.2921, 36.8219});
        countryCoordinates.put("Kiribati", new double[]{-3.3704, -168.7340});
        countryCoordinates.put("Korea, Democratic People's Republic of", new double[]{39.0392, 125.7625});
        countryCoordinates.put("Korea, Republic of", new double[]{37.5665, 126.9780});
        countryCoordinates.put("Kuwait", new double[]{29.3759, 47.9774});
        countryCoordinates.put("Kyrgyzstan", new double[]{42.8746, 74.5698});
        countryCoordinates.put("Lao People's Democratic Republic", new double[]{17.9757, 102.6331});
        countryCoordinates.put("Latvia", new double[]{56.9496, 24.1052});
        countryCoordinates.put("Lebanon", new double[]{33.8547, 35.8623});
        countryCoordinates.put("Lesotho", new double[]{-29.6100, 28.2336});
        countryCoordinates.put("Liberia", new double[]{6.2907, -10.7605});
        countryCoordinates.put("Libya", new double[]{32.8872, 13.1913});
        countryCoordinates.put("Liechtenstein", new double[]{47.1660, 9.5554});
        countryCoordinates.put("Lithuania", new double[]{54.6872, 25.2797});
        countryCoordinates.put("Luxembourg", new double[]{49.8153, 6.1296});
        countryCoordinates.put("Macao", new double[]{22.1987, 113.5439});
        countryCoordinates.put("Macedonia, the former Yugoslav Republic of", new double[]{42.0042, 21.4335});
        countryCoordinates.put("Madagascar", new double[]{-18.8792, 47.5079});
        countryCoordinates.put("Malawi", new double[]{-13.2543, 34.3015});
        countryCoordinates.put("Malaysia", new double[]{3.1390, 101.6869});
        countryCoordinates.put("Maldives", new double[]{3.2028, 73.2207});
        countryCoordinates.put("Mali", new double[]{12.6392, -8.0029});
        countryCoordinates.put("Malta", new double[]{35.9375, 14.3754});
        countryCoordinates.put("Marshall Islands", new double[]{24.7594, 168.7340});
        countryCoordinates.put("Martinique", new double[]{14.6415, -61.0242});
        countryCoordinates.put("Mauritania", new double[]{18.0735, -15.9582});
        countryCoordinates.put("Mauritius", new double[]{-20.3484, 57.5522});
        countryCoordinates.put("Mayotte", new double[]{-12.8275, 45.1662});
        countryCoordinates.put("Mexico", new double[]{19.4326, -99.1332});
        countryCoordinates.put("Micronesia, Federated States of", new double[]{7.4256, 150.5508});
        countryCoordinates.put("Moldova, Republic of", new double[]{47.0105, 28.8638});
        countryCoordinates.put("Monaco", new double[]{43.7384, 7.4246});
        countryCoordinates.put("Mongolia", new double[]{47.8864, 106.9057});
        countryCoordinates.put("Montenegro", new double[]{42.7087, 19.3744});
        countryCoordinates.put("Montserrat", new double[]{16.7425, -62.1874});
        countryCoordinates.put("Morocco", new double[]{33.9716, -6.8498});
        countryCoordinates.put("Mozambique", new double[]{-25.9692, 32.5732});
        countryCoordinates.put("Myanmar", new double[]{19.7633, 96.0785});
        countryCoordinates.put("Namibia", new double[]{-22.9576, 18.4904});
        countryCoordinates.put("Nauru", new double[]{-0.5228, 166.9315});
        countryCoordinates.put("Nepal", new double[]{27.7172, 85.3240});
        countryCoordinates.put("Netherlands", new double[]{52.3676, 4.9041});
        countryCoordinates.put("New Caledonia", new double[]{-22.2758, 166.4581});
        countryCoordinates.put("New Zealand", new double[]{-41.2865, 174.7762});
        countryCoordinates.put("Nicaragua", new double[]{12.1150, -86.2362});
        countryCoordinates.put("Niger", new double[]{13.5116, 2.1254});
        countryCoordinates.put("Nigeria", new double[]{9.0765, 7.3986});
        countryCoordinates.put("Niue", new double[]{-19.0544, -169.8672});
        countryCoordinates.put("Norfolk Island", new double[]{-29.0408, 167.9547});
        countryCoordinates.put("Northern Mariana Islands", new double[]{17.3308, 145.3846});
        countryCoordinates.put("Norway", new double[]{59.9139, 10.7522});
        countryCoordinates.put("Oman", new double[]{23.5859, 58.4059});
        countryCoordinates.put("Pakistan", new double[]{33.7294, 73.0931});
        countryCoordinates.put("Palau", new double[]{7.5000, 134.6242});
        countryCoordinates.put("Palestinian Territory, Occupied", new double[]{31.9522, 35.2332});
        countryCoordinates.put("Panama", new double[]{8.5380, -80.7821});
        countryCoordinates.put("Papua New Guinea", new double[]{-9.4438, 147.1803});
        countryCoordinates.put("Paraguay", new double[]{-25.2637, -57.5759});
        countryCoordinates.put("Peru", new double[]{-12.0464, -77.0428});
        countryCoordinates.put("Philippines", new double[]{14.5995, 120.9842});
        countryCoordinates.put("Pitcairn", new double[]{-24.7036, -127.4393});
        countryCoordinates.put("Poland", new double[]{52.2297, 21.0122});
        countryCoordinates.put("Portugal", new double[]{38.7223, -9.1393});
        countryCoordinates.put("Puerto Rico", new double[]{18.2208, -66.5901});
        countryCoordinates.put("Qatar", new double[]{25.2854, 51.5310});
        countryCoordinates.put("Réunion", new double[]{-21.1151, 55.5364});
        countryCoordinates.put("Romania", new double[]{44.4268, 26.1025});
        countryCoordinates.put("Russian Federation", new double[]{55.7558, 37.6176});
        countryCoordinates.put("Rwanda", new double[]{-1.9706, 30.1044});
        countryCoordinates.put("Saint Barthélemy", new double[]{17.9000, -62.8500});
        countryCoordinates.put("Saint Helena, Ascension and Tristan da Cunha", new double[]{-24.1434, -10.0307});
        countryCoordinates.put("Saint Kitts and Nevis", new double[]{17.3578, -62.7830});
        countryCoordinates.put("Saint Lucia", new double[]{14.0101, -60.9875});
        countryCoordinates.put("Saint Martin (French part)", new double[]{18.0708, -63.0501});
        countryCoordinates.put("Saint Pierre and Miquelon", new double[]{46.8852, -56.3159});
        countryCoordinates.put("Saint Vincent and the Grenadines", new double[]{12.9843, -61.2872});
        countryCoordinates.put("Samoa", new double[]{-13.7590, -172.1046});
        countryCoordinates.put("San Marino", new double[]{43.9420, 12.4580});
        countryCoordinates.put("Sao Tome and Principe", new double[]{0.1864, 6.6131});
        countryCoordinates.put("Saudi Arabia", new double[]{24.7136, 46.6753});
        countryCoordinates.put("Senegal", new double[]{14.7645, -17.3660});
        countryCoordinates.put("Serbia", new double[]{44.7866, 20.4489});
        countryCoordinates.put("Seychelles", new double[]{-4.6574, 55.4540});
        countryCoordinates.put("Sierra Leone", new double[]{8.4657, -11.7799});
        countryCoordinates.put("Singapore", new double[]{1.3521, 103.8198});
        countryCoordinates.put("Sint Maarten (Dutch part)", new double[]{18.0425, -63.0548});
        countryCoordinates.put("Slovakia", new double[]{48.1486, 17.1077});
        countryCoordinates.put("Slovenia", new double[]{46.0569, 14.5058});
        countryCoordinates.put("Solomon Islands", new double[]{-9.6457, 160.1562});
        countryCoordinates.put("Somalia", new double[]{2.0469, 45.3182});
        countryCoordinates.put("South Africa", new double[]{-25.7479, 28.2293});
        countryCoordinates.put("South Georgia and the South Sandwich Islands", new double[]{-54.4296, -36.5879});
        countryCoordinates.put("South Sudan", new double[]{4.8594, 31.5713});
        countryCoordinates.put("Spain", new double[]{40.4168, -3.7038});
        countryCoordinates.put("Sri Lanka", new double[]{6.9271, 79.8612});
        countryCoordinates.put("Sudan", new double[]{15.5007, 32.5599});
        countryCoordinates.put("Suriname", new double[]{5.8520, -55.2038});
        countryCoordinates.put("Svalbard and Jan Mayen", new double[]{77.5536, 23.6702});
        countryCoordinates.put("Swaziland", new double[]{-26.5225, 31.4659});
        countryCoordinates.put("Sweden", new double[]{59.3293, 18.0686});
        countryCoordinates.put("Switzerland", new double[]{46.9480, 7.4474});
        countryCoordinates.put("Syrian Arab Republic", new double[]{33.5138, 36.2765});
        countryCoordinates.put("Taiwan, Province of China", new double[]{25.0320, 121.5654});
        countryCoordinates.put("Tajikistan", new double[]{38.8610, 71.2761});
        countryCoordinates.put("Tanzania, United Republic of", new double[]{-6.3690, 34.8888});
        countryCoordinates.put("Thailand", new double[]{13.7563, 100.5018});
        countryCoordinates.put("Timor-Leste", new double[]{-8.8742, 125.7275});
        countryCoordinates.put("Togo", new double[]{6.1228, 1.2255});
        countryCoordinates.put("Tokelau", new double[]{-8.9670, -171.8570});
        countryCoordinates.put("Tonga", new double[]{-21.1789, -175.1982});
        countryCoordinates.put("Trinidad and Tobago", new double[]{10.6918, -61.2225});
        countryCoordinates.put("Tunisia", new double[]{36.8065, 10.1815});
        countryCoordinates.put("Turkey", new double[]{39.9334, 32.8597});
        countryCoordinates.put("Turkmenistan", new double[]{37.9601, 58.3261});
        countryCoordinates.put("Turks and Caicos Islands", new double[]{21.4691, -71.1419});
        countryCoordinates.put("Tuvalu", new double[]{-7.1095, 177.6493});
        countryCoordinates.put("Uganda", new double[]{0.3476, 32.5825});
        countryCoordinates.put("Ukraine", new double[]{50.4501, 30.5234});
        countryCoordinates.put("United Arab Emirates", new double[]{24.2990, 54.6963});
        countryCoordinates.put("United Kingdom", new double[]{51.5074, -0.1278});
        countryCoordinates.put("United States", new double[]{38.9072, -77.0369});
        countryCoordinates.put("United States Minor Outlying Islands", new double[]{16.0000, -169.0000});
        countryCoordinates.put("Uruguay", new double[]{-34.9011, -56.1645});
        countryCoordinates.put("Uzbekistan", new double[]{41.2995, 69.2401});
        countryCoordinates.put("Vanuatu", new double[]{-17.7334, 168.3273});
        countryCoordinates.put("Venezuela, Bolivarian Republic of", new double[]{10.4806, -66.9036});
        countryCoordinates.put("Viet Nam", new double[]{10.8231, 106.6297});
        countryCoordinates.put("Virgin Islands, British", new double[]{18.4207, -64.6400});
        countryCoordinates.put("Virgin Islands, U.S.", new double[]{18.3358, -64.8963});
        countryCoordinates.put("Wallis and Futuna", new double[]{-13.7687, -177.1562});
        countryCoordinates.put("Western Sahara", new double[]{24.2155, -12.8858});
        countryCoordinates.put("Yemen", new double[]{15.3694, 44.1910});
        countryCoordinates.put("Zambia", new double[]{-15.3875, 28.3228});
        countryCoordinates.put("Zimbabwe", new double[]{-17.8292, 31.0522});
    }
}
