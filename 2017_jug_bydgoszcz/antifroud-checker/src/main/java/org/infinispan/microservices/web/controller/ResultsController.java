package org.infinispan.microservices.web.controller;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResultsController {

   private static final String CACHE_NAME = "transactions_after_antifraud_check";

   private final RemoteCache<String, Integer> remoteCache;

   public ResultsController(RemoteCacheManager remoteCacheManager) {
      this.remoteCache = remoteCacheManager.getCache(CACHE_NAME);
   }

   @RequestMapping("/results")
   public String greeting(Model model) {
      model.addAttribute("total_results", remoteCache.size());
      return "results";
   }

}
