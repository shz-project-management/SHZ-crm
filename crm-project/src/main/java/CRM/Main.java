package CRM;

import CRM.entity.NotificationSetting;
import CRM.repository.NotificationSettingRepository;
import CRM.utils.enums.Notifications;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Main {

    @Autowired
    private NotificationSettingRepository notificationSettingRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    InitializingBean initNotificationsInDB(){
        return ()->{
            for (Notifications notification : Notifications.values()) {
                if(!notificationSettingRepository.findByName(notification.name).isPresent()){
                    NotificationSetting notificationSetting = new NotificationSetting(0L, notification.name, notification.description, notification.number);
                    notificationSettingRepository.save(notificationSetting);
                }
            }
        };
    }
}