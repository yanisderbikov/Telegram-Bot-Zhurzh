//package com.zhurzh.node.bot.branches;
//
//import org.telegram.telegrambots.meta.api.objects.Update;
//
//
//import com.zhurzh.commonjpa.dao.AppUserDAO;
//import com.zhurzh.commonjpa.entity.AppUser;
//import com.zhurzh.commonjpa.enums.BranchStatus;
//import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
//import com.zhurzh.commonutils.model.Branches;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.Map;
//import java.util.function.BiFunction;
//
////@Component
//@RequiredArgsConstructor
//@Log4j
//public class BranchesManagerTest {
//    private final ApplicationContext applicationContext;
//    private final AppUserDAO appUserDAO;
//    private final CommandsManager commandsManager;
//
//    private Map<String, Branches> branchesMap;
//
//    @PostConstruct
//    private void init() {
//        branchesMap = applicationContext.getBeansOfType(Branches.class);
//    }
//
////    @Override
//    public void consume(Update update) {
//        findAndProcessBranch(update, Branches::manageText);
//    }
//
//    private void findAndProcessBranch(Update update, BiFunction<Branches, Update, Void> branchProcessor) {
//        Branches branch = findBranchForUpdate(update);
//        if (branch != null) {
//            branchProcessor.apply(branch, update);
//        }
//    }
//
//    private Branches findBranchForUpdate(Update update) {
//        AppUser appUser = commandsManager.findOrSaveAppUser(update);
//        BranchStatus status = determineBranchStatus(update, appUser);
//        appUser.setBranchStatus(status);
//        appUserDAO.save(appUser);
//
//        // Ищем соответствующий бин для статуса ветвления
//        return branchesMap.values().stream()
//                .filter(b -> b.getBranchStatus() == status)
//                .findFirst()
//                .orElse(null);
//    }
//
//    private BranchStatus determineBranchStatus(Update update, AppUser appUser) {
//        if (update.hasCallbackQuery()) {
//            String callbackData = update.getCallbackQuery().getData();
//
//            // Примеры условий для определения статуса ветки
//            if ("/start".equals(callbackData)) {
//                return BranchStatus.START;
//            } else if ("/menu".equals(callbackData)) {
//                return BranchStatus.MENU;
//            } else if ("/priceList".equals(callbackData)) { // Предположим, что есть такая команда
//                return BranchStatus.PRICE_LIST;
//            } else if ("/checkOrder".equals(callbackData)) { // Предположим, что есть такая команда
//                return BranchStatus.CHECK_ORDER;
//            } else if ("/order".equals(callbackData)) { // Предположим, что есть такая команда
//                return BranchStatus.ORDER;
//            }
//        }
//
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String messageText = update.getMessage().getText();
//
//            // Аналогичные условия для текстовых сообщений
//            if ("/start".equals(messageText)) {
//                return BranchStatus.START;
//            }
//            else if ("/menu".equals(messageText)) {
//                return BranchStatus.MENU;
//            } else if ("/priceList".equals(messageText)) {
//                return BranchStatus.PRICE_LIST;
//            } else if ("/checkOrder".equals(messageText)) {
//                return BranchStatus.CHECK_ORDER;
//            } else if ("/order".equals(messageText)) {
//                return BranchStatus.ORDER;
//            }
//        }
//    }
//}
