/*
* File   : $Source: /alkacon/cvs/opencms/src/com/opencms/workplace/Attic/CmsAdminModuleCreate.java,v $
* Date   : $Date: 2002/12/07 09:28:22 $
* Version: $Revision: 1.23 $
*
* This library is part of OpenCms -
* the Open Source Content Mananagement System
*
* Copyright (C) 2001  The OpenCms Group
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* For further information about OpenCms, please see the
* OpenCms Website: http://www.opencms.org
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.opencms.workplace;

import com.opencms.core.A_OpenCms;
import com.opencms.core.CmsException;
import com.opencms.core.I_CmsConstants;
import com.opencms.core.I_CmsLogChannels;
import com.opencms.core.I_CmsSession;
import com.opencms.file.CmsObject;
import com.opencms.file.CmsRegistry;
import com.opencms.file.I_CmsRegistry;
import com.opencms.template.CmsXmlTemplateFile;
import com.opencms.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Template class for displaying OpenCms workplace administration module create.
 *
 * Creation date: (27.10.00 10:28:08)
 * @author: Hanjo Riege
 */
public class CmsAdminModuleCreate extends CmsWorkplaceDefault implements I_CmsConstants {
    private final String C_PACKETNAME = "packetname";
    private final String C_STEP = "step";
    private final String C_VERSION = "version";
    private final String C_MODULENAME = "modulename";
    private final String C_DESCRIPTION = "description";
    private final String C_VIEW = "view";
    private final String C_ADMINPOINT = "adminpoint";
    private final String C_MAINTENANCE = "maintenance";
    private final String C_PUBLISHCLASS = "publishclass";    
    private final String C_AUTHOR = "author";
    private final String C_EMAIL = "email";
    private final String C_DATE = "date";
    private final String C_SESSION_DATA = "module_create_data";
    private final String C_MODULE_TYPE = "moduletype";

    /**
     *  Checks if the name is correct.
     *  @param name the name to check..
     */
    private boolean checkName(String name) {
        if(name == null || name.length() == 0 || name.trim().length() == 0) {
            return false;
        }
        for(int i = 0;i < name.length();i++) {
            char c = name.charAt(i);
            if(((c < 'a') || (c > 'z')) && ((c < '0') || (c > '9')) && ((c < 'A') || (c > 'Z')) && (c != '-') && (c != '.') && (c != '_') && (c != '~')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the content of a defined section in a given template file and its subtemplates
     * with the given parameters.
     *
     * @see getContent(CmsObject cms, String templateFile, String elementName, Hashtable parameters)
     * @param cms CmsObject Object for accessing system resources.
     * @param templateFile Filename of the template file.
     * @param elementName Element name of this template in our parent template.
     * @param parameters Hashtable with all template class parameters.
     * @param templateSelector template section that should be processed.
     */
    public byte[] getContent(CmsObject cms, String templateFile, String elementName, Hashtable parameters, String templateSelector) throws CmsException {
        if(I_CmsLogChannels.C_PREPROCESSOR_IS_LOGGING && A_OpenCms.isLogging() && C_DEBUG) {
            A_OpenCms.log(C_OPENCMS_DEBUG, this.getClassName() + "getting content of element " + ((elementName == null) ? "<root>" : elementName));
            A_OpenCms.log(C_OPENCMS_DEBUG, this.getClassName() + "template file is: " + templateFile);
            A_OpenCms.log(C_OPENCMS_DEBUG, this.getClassName() + "selected template section is: " + ((templateSelector == null) ? "<default>" : templateSelector));
        }
        CmsXmlTemplateFile templateDocument = getOwnTemplateFile(cms, templateFile, elementName, parameters, templateSelector);
        //CmsRequestContext reqCont = cms.getRequestContext();
        I_CmsRegistry reg = cms.getRegistry();
        I_CmsSession session = cms.getRequestContext().getSession(true);
        String step = (String)parameters.get(C_STEP);
        SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy");
        if((step == null) || "".equals(step)) {
            templateDocument.setData(C_PACKETNAME, "");
            templateDocument.setData(C_VERSION, "1");
            templateDocument.setData(C_MODULENAME, "");
            templateDocument.setData(C_DESCRIPTION, "");
            templateDocument.setData(C_VIEW, "");
            templateDocument.setData(C_ADMINPOINT, "");
            templateDocument.setData(C_MAINTENANCE, "");
            templateDocument.setData(C_PUBLISHCLASS, "");            
            templateDocument.setData(C_AUTHOR, "");
            templateDocument.setData(C_EMAIL, "");
            templateDocument.setData(C_MODULE_TYPE, "checked");
            //  set the current date:
            templateDocument.setData(C_DATE, dateFormat.format(new Date()));
        }else {
            if("OK".equals(step) || "Ok".equals(step)) {
                String packagename = (String)parameters.get(C_PACKETNAME);
                String modulename = (String)parameters.get(C_MODULENAME);
                String version = (String)parameters.get(C_VERSION);
                String description = (String)parameters.get(C_DESCRIPTION);
                String view = (String)parameters.get(C_VIEW);
                String adminpoint = (String)parameters.get(C_ADMINPOINT);
                String maintenance = (String)parameters.get(C_MAINTENANCE);
                String publishclass = (String)parameters.get(C_PUBLISHCLASS);
                String author = (String)parameters.get(C_AUTHOR);
                String email = (String)parameters.get(C_EMAIL);
                String createDate = (String)parameters.get(C_DATE);
                String moduleType = (String)parameters.get(C_MODULE_TYPE);
                
                boolean moduleExists = reg.moduleExists(packagename);
                int v = -1;
                try {
                    v = Integer.parseInt(version);
                }catch(Exception e) {
                }
                if((!checkName(packagename)) || (version == null) || ("".equals(version)) || moduleExists || (v < 0)) {
                    Hashtable sessionData = new Hashtable();
                    sessionData.put(C_MODULENAME, getStringValue(modulename));
                    sessionData.put(C_VERSION, getStringValue(version));
                    sessionData.put(C_DESCRIPTION, getStringValue(description));
                    sessionData.put(C_VIEW, getStringValue(view));
                    sessionData.put(C_ADMINPOINT, getStringValue(adminpoint));
                    sessionData.put(C_MAINTENANCE, getStringValue(maintenance));
                    sessionData.put(C_PUBLISHCLASS, getStringValue(publishclass));                    
                    sessionData.put(C_AUTHOR, getStringValue(author));
                    sessionData.put(C_EMAIL, getStringValue(email));
                    sessionData.put(C_DATE, getStringValue(createDate));
                    sessionData.put(C_MODULE_TYPE, getStringValue(moduleType));
                    session.putValue(C_SESSION_DATA, sessionData);
                    if(moduleExists) {
                        templateSelector = "errorexists";
                    }else {
                        templateSelector = "errornoname";
                    }
                }else {
                    tryToCreateFolder(cms, "/system/", "modules");
                    // create the module (first test if we are in a project including /system/
                    try {
                        cms.createResource(C_VFS_PATH_MODULES, packagename, C_TYPE_FOLDER_NAME);
                    }catch(CmsException e) {
                        if(e.getType() != CmsException.C_FILE_EXISTS) {
                            // couldn't create Module
                            templateDocument.setData("details", Utils.getStackTrace(e));
                            return startProcessing(cms, templateDocument, elementName, parameters, "errorProject");
                        }else {
                            try {
                                cms.readFolder(C_VFS_PATH_MODULES + packagename + "/");
                            }catch(CmsException ex) {
                                // folder exist but is deleted
                                templateDocument.setData("details", "Sorry, you have to publish this Project and create a new one.\n" + Utils.getStackTrace(e));
                                return startProcessing(cms, templateDocument, elementName, parameters, "errorProject");
                            }
                        }
                    }
                    long createDateLong = 0;
                    try {
                        createDateLong = dateFormat.parse(createDate).getTime();
                    }catch(Exception exc) {
                        createDateLong = (new Date()).getTime();
                    }
                    reg.createModule(packagename, getStringValue(modulename), getStringValue(description), getStringValue(author), createDateLong, v);
                    reg.setModuleAuthorEmail(packagename, getStringValue(email));
                    reg.setModuleMaintenanceEventClass(packagename, getStringValue(maintenance));
                    reg.setModulePublishClass(packagename, getStringValue(publishclass));
                    
                    boolean isSimpleModule = false;
                    if (moduleType!=null && moduleType.equals("checked")) {
                        reg.setModuleType( packagename, CmsRegistry.C_MODULE_TYPE_SIMPLE );
                        isSimpleModule = true;
                    } else {
                        reg.setModuleType( packagename, CmsRegistry.C_MODULE_TYPE_TRADITIONAL );
                    }

                    /*
                    // Moduldemo folder is deprecated since 5.0 beta 2
                    tryToCreateFolder(cms, "/", "moduledemos");
                    tryToCreateFolder(cms, "/moduledemos/", packetname);
                    */
                    
                    String modulePath = C_VFS_PATH_MODULES + packagename + "/";
                    
                    // as default dont export any module data                    
                    cms.writeProperty(modulePath, C_PROPERTY_EXPORT, "false");
                    
                    // create the class folder:
                    tryToCreateFolder(cms, modulePath, "classes");
                    // create all package folders beneth class folder
                    Vector cFolders = new Vector();
                    String workString = packagename;
                    while(workString.lastIndexOf('.') > -1) {
                        cFolders.addElement(workString.substring(workString.lastIndexOf('.') + 1));
                        workString = workString.substring(0, workString.lastIndexOf('.'));
                    }
                    tryToCreateFolder(cms, modulePath+"classes/", workString);
                    workString = modulePath + "classes/" + workString + "/";
                    for(int i = cFolders.size() - 1;i >= 0;i--) {
                        tryToCreateFolder(cms, workString, (String)cFolders.elementAt(i));
                        workString = workString + (String)cFolders.elementAt(i) + "/";
                    }                    
                    // create the lib folder, will get exportet to the "real" file system
                    tryToCreateFolder(cms, modulePath, "lib");
                    // create the templates folder
                    tryToCreateFolder(cms, modulePath, I_CmsWpConstants.C_VFS_DIR_TEMPLATES);
                    // create the "default_bodies" folder 
                    tryToCreateFolder(cms, modulePath, I_CmsWpConstants.C_VFS_DIR_DEFAULTBODIES);
                    // crate "elements" folder
                    tryToCreateFolder(cms, modulePath, "elements");

                    if (isSimpleModule) {
                        // simple module type 
                        reg.setModuleDocumentPath(packagename, modulePath + "index.html");                        
                    } else {
                        // traditional module type, create more directories
                        tryToCreateFolder(cms, modulePath, "contenttemplates");
                        tryToCreateFolder(cms, modulePath, "frametemplates");
                        tryToCreateFolder(cms, modulePath, "doc");
                        reg.setModuleDocumentPath(packagename, modulePath + "doc/index.html");
                    }
                    // initialize if we need a 'locales' subdirectory in our new module
                    boolean needsLocaleDir = !isSimpleModule;
                                        
                    if("checked".equals(view)) {
                        reg.setModuleView(packagename, packagename.replace('.', '_') + ".view", modulePath + "view/index.html");
                        tryToCreateFolder(cms, modulePath, "view");
                        needsLocaleDir = true;
                    }
                    if("checked".equals(adminpoint)) {
                        tryToCreateFolder(cms, modulePath, "administration");
                        // create "pics" folder (required for workplace extension images)
                        tryToCreateFolder(cms, modulePath, "pics");
                        needsLocaleDir = true;
                    }
                    if (needsLocaleDir) {
                        // create "locales" folder (required for workplace extension resource files)
                        tryToCreateFolder(cms, modulePath, I_CmsWpConstants.C_VFS_DIR_LOCALES);
                        tryToCreateFolder(cms, modulePath + I_CmsWpConstants.C_VFS_DIR_LOCALES, I_CmsWpConstants.C_DEFAULT_LANGUAGE);
                    }
                    
                    try {
                        cms.getRequestContext().getResponse().sendCmsRedirect(getConfigFile(cms).getWorkplaceAdministrationPath() + "module/index.html");
                    }catch(Exception e) {
                        throw new CmsException("Redirect fails :system/workplace/administration/module/index.html", CmsException.C_UNKNOWN_EXCEPTION, e);
                    }
                    return null;
                }
            }else {
                if("fromerrorpage".equals(step)) {
                    Hashtable sessionData = (Hashtable)session.getValue(C_SESSION_DATA);
                    session.removeValue(C_SESSION_DATA);
                    templateDocument.setData(C_PACKETNAME, "");
                    templateDocument.setData(C_VERSION, (String)sessionData.get(C_VERSION));
                    templateDocument.setData(C_MODULENAME, (String)sessionData.get(C_MODULENAME));
                    templateDocument.setData(C_DESCRIPTION, (String)sessionData.get(C_DESCRIPTION));
                    templateDocument.setData(C_VIEW, (String)sessionData.get(C_VIEW));
                    templateDocument.setData(C_ADMINPOINT, (String)sessionData.get(C_ADMINPOINT));
                    templateDocument.setData(C_MAINTENANCE, (String)sessionData.get(C_MAINTENANCE));
                    templateDocument.setData(C_PUBLISHCLASS, (String)sessionData.get(C_PUBLISHCLASS));                    
                    templateDocument.setData(C_AUTHOR, (String)sessionData.get(C_AUTHOR));
                    templateDocument.setData(C_EMAIL, (String)sessionData.get(C_EMAIL));
                    templateDocument.setData(C_DATE, (String)sessionData.get(C_DATE));
                    templateDocument.setData(C_MODULE_TYPE, (String)sessionData.get(C_MODULE_TYPE));
                    templateSelector = "";
                }
            }
        }

        // Now load the template file and start the processing
        return startProcessing(cms, templateDocument, elementName, parameters, templateSelector);
    }

    /**
     * returns the String or "" if it is null.
     * Creation date: (29.10.00 16:05:38)
     * @return java.lang.String
     * @param param java.lang.String
     */
    private String getStringValue(String param) {
        if(param == null) {
            return "";
        }
        return param;
    }

    /**
     * Indicates if the results of this class are cacheable.
     *
     * @param cms CmsObject Object for accessing system resources
     * @param templateFile Filename of the template file
     * @param elementName Element name of this template in our parent template.
     * @param parameters Hashtable with all template class parameters.
     * @param templateSelector template section that should be processed.
     * @return <EM>true</EM> if cacheable, <EM>false</EM> otherwise.
     */
    public boolean isCacheable(CmsObject cms, String templateFile, String elementName, Hashtable parameters, String templateSelector) {
        return false;
    }

    /**
     * Insert the method's description here.
     * Creation date: (03.11.00 08:23:13)
     * @param param com.opencms.file.CmsObject
     * @param folder java.lang.String
     * @param newFolder java.lang.String
     */
    private void tryToCreateFolder(CmsObject cms, String folder, String newFolder) {
        try {
            cms.createResource(folder, newFolder, C_TYPE_FOLDER_NAME);
        }catch(Exception e) {
        }
    }
}
